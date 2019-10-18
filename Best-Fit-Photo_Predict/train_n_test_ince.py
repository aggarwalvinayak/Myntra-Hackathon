'''
This is the main training profile.
'''
from fashion_input import *
import os
import tensorflow as tf
import time
from datetime import datetime
from simple_resnet import *
from hyper_parameters import *
import sys
from keras.applications.inception_v3 import InceptionV3
from keras.preprocessing import image
from keras.models import Model
from keras.utils import Sequence

from keras.layers import Dense, GlobalAveragePooling2D
from keras import backend as K
# from keras.model

from keras.optimizers import SGD

TRAIN_DIR = 'logs_' + FLAGS.version + '/'
TRAIN_LOG_PATH = FLAGS.version + '_error.csv'

REPORT_FREQ = 100
TRAIN_BATCH_SIZE = 32
VALI_BATCH_SIZE = 25
TEST_BATCH_SIZE = 1
FULL_VALIDATION = False
Error_EMA = 0.98

STEP_TO_TRAIN = 10000
DECAY_STEP0 = 7000
DECAY_STEP1 = 9000

#config = tf.ConfigProto( device_count = {'GPU': 1 , 'CPU': 8} ) 
#sess = tf.Session(config=config) 
#keras.backend.set_session(sess)

def generate_validation_batch(df):
    '''
    :param df: a pandas dataframe with validation image paths and the corresponding labels
    :return: two random numpy arrays: validation_batch and validation_label
    '''
    offset = np.random.choice(len(df) - VALI_BATCH_SIZE, 1)[0]
    validation_df = df.iloc[offset:offset+VALI_BATCH_SIZE, :]

    validation_batch, validation_label, validation_bbox_label = load_data_numpy(validation_df)
    return validation_batch, validation_label, validation_bbox_label



class MY_Generator(Sequence):
 
    def __init__(self, image_filenames, labels, batch_size,shape):
        from keras.utils import to_categorical
        labels= to_categorical(labels)
        self.image_filenames, self.labels = image_filenames, labels
        self.batch_size = batch_size
        self.shape = shape
 
    def __len__(self):
        return int(np.ceil(len(self.image_filenames) / float(self.batch_size)))
 
    def __getitem__(self, idx):
        batch_x = self.image_filenames[idx * self.batch_size:(idx + 1) * self.batch_size]
        batch_y = self.labels[idx * self.batch_size:(idx + 1) * self.batch_size]
 
        return np.array([
            cv2.cvtColor(
            cv2.resize(cv2.imread(file_name), (self.shape[0], self.shape[1]))
            , cv2.COLOR_BGR2RGB)/255
               for file_name in batch_x]), np.array(batch_y,dtype=int)

def lol(df_tr,df_va):
    

    # create the base pre-trained model
    base_model = InceptionV3(weights='imagenet', include_top=False)

    # add a global spatial average pooling layer
    x = base_model.output
    x = GlobalAveragePooling2D()(x)
    # let's add a fully-connected layer
    x = Dense(1024, activation='relu')(x)
    # and a logistic layer -- let's say we have 200 classes
    predictions = Dense(49, activation='softmax')(x)

    # this is the model we will train
    model = Model(inputs=base_model.input, outputs=predictions)

    # first: train only the top layers (which were randomly initialized)
    # i.e. freeze all convolutional InceptionV3 layers
    for layer in base_model.layers:
        layer.trainable = False


    # compile the model (should be done *after* setting layers to non-trainable)
    model.compile(optimizer='rmsprop', loss='categorical_crossentropy')

    # train the model on the new data for a few epochs
    # model.fit_generator(generator=train_gen,steps_per_epoch)
    train_gen=MY_Generator(df_tr['image_path'],df_tr['category'],32,(128,128,3))
    print("&&&&&&&&&&&&&&&&",train_gen[0])
    vali_gen=MY_Generator(df_va['image_path'],df_va['category'],32,(128,128,3))

    model.fit_generator(generator=train_gen,
                                  steps_per_epoch=(len(df_tr) // 32),
                                  epochs=1,
                                  verbose=1)
                                  # validation_data=vali_gen,
                                  # validation_steps=(len(df_va) // 32))
                                  # )

    # at this point, the top layers are well trained and we can start fine-tuning
    # convolutional layers from inception V3. We will freeze the bottom N layers
    # and train the remaining top layers.

    # let's visualize layer names and layer indices to see how many layers
    # we should freeze:
    for i, layer in enumerate(base_model.layers):
       print(i, layer.name)

    # # we chose to train the top 2 inception blocks, i.e. we will freeze
    # # the first 249 layers and unfreeze the rest:
    for layer in model.layers[:249]:
       layer.trainable = False
    for layer in model.layers[249:]:
       layer.trainable = True
    model.save("./modelpre")

    # we need to recompile the model for these modifications to take effect
    # we use SGD with a low learning rate
    model.compile(optimizer=Adam(lr=0.001), loss='categorical_crossentropy')

    # we train our model again (this time fine-tuning the top 2 inception blocks
    # alongside the top Dense layers
    # model.fit_generator(...)
    model.fit_generator(generator=train_gen,steps_per_epoch=(len(df_tr) // 32),epochs=2, verbose=1)
    # validation_data=vali_gen,validation_steps=(len(df_va) // 32))
    model.save("./model")


df_tr=prepare_df(FLAGS.train_path,usecols=['image_path','category'])
df_va=prepare_df(FLAGS.vali_path,usecols=['image_path','category'])

lol(df_tr,df_va)



# train = Train()

# download_img()
# train.test()
# aa,bb=fn("outpred.csv")
# upload_var(aa,bb)
