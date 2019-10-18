# Myntra-Hackathon HACKERRAMP'19
----------------------------------------------------------------------------------------------------------
# Auxiliary Classifier Generative Adversarial Networks (Crowdfunding)
 
The Auxiliary Classifier GAN, or AC-GAN for short, is an extension of the conditional GAN that changes the discriminator to predict the class label of a given image rather than receive it as input. It has the effect of stabilizing the training process and allowing the generation of large high-quality images whilst learning a representation in the latent space that is independent of the class label.
The architecture is comprised of both a generator model that takes random points from a latent space as input and generates images, and a discriminator for classifying images as either real (from the dataset) or fake (generate). Both models are then trained simultaneously in a zero-sum game.
The discriminator is provided with both an image and the class label and must classify whether the image is real or fake as before.
The main difference is in the discriminator model, which is only provided with the image as input, unlike the conditional GAN that is provided with the image and class label as input. The discriminator model must then predict whether the given image is real or fake as before, and must also predict the class label of the image.
**Generator Model:**
-   **Input:** Random point from the latent space, and the class label.
-   **Output:**  Generated image.
 
**Discriminator Model:**
-   **Input:**  Image.
-  **Output:** Probability that the provided image is real, probability of the image belonging to each known class.
 
 ## AC-GAN Models
we have developed the generator, discriminator, and composite models for the AC-GAN.
 
**![](https://lh4.googleusercontent.com/lWKnoZOL2WHII8xFHvUoemo-Bs3tprnq9ulQXQKQQsW1KooRyoQBeVpY1jf6AtptuVwOVHsfFBt8zXkapfJsQTi6v0_QXWbhp0xduq7liJZJ_1ieRSjxCXYQ2HgfrfEEZaABg847)**
## AC-GAN Discriminator Model
The model can be defined as per the DCGAN architecture. That is, using Gaussian weight initialization, batch normalization, LeakyReLU, Dropout, and a 2×2 stride for downsampling instead of pooling layers.
The model must be trained with two loss functions, binary cross entropy for the first output layer, and categorical cross-entropy loss for the second output layer.
The model is fit using the Adam version of stochastic gradient descent with a small learning rate and modest momentum, as is recommended for DCGANs.
**![](https://lh6.googleusercontent.com/JdH3Xc6bHZ1H9jq0URgyCXVMKjvB8oYPjzs5rMPMeUF3ES2_raSXPlEuiwbmJhJ-uEm_TsNDbiEtN0nhmCIfI2CIvk1FAwvSa4B6h75RK15jVhMEzpjyn15Sm6Zx6C8WfQDHcA1e)![](https://lh3.googleusercontent.com/-XYhixQb-REfl2OmuWtl5PRkLbVr1mnWD34_7uO8ZDb1Fsi4wpMnLccdlgmVt-rCr9CSo94sLnapvU_e4yscMQ85kUoUVwsvTkQMTzR7P1-kCNB-oAik8Ny4jjutDJ0z5LqlLmBZ)**
## AC-GAN Generator Model Training
The AC-GAN paper describes the AC-GAN generator model taking a vector input that is a concatenation of the point in latent space (100 dimensions) and the one hot encoded class label (10 dimensions) that is 110 dimensions.
The output of the generator is a single feature map or grayscale image with the shape 28×28 and pixel values in the range [-1, 1]  for the upscaling layers instead of LeakyReLU.
**![](https://lh6.googleusercontent.com/0o59XTEq7mS5qPWqZAnrb9tkWfrxk_LW7Ql0iAA2TMjZml9n684joBqR3KL5ooKo1hoj9H8XY_0Q-gu4rKTX30Lx19PJxfpEqDfwQu2m9gpnPevJZVk7F5JkJq1IcuVNWt-ptWyP)**
## Training
We are now ready to fit the GAN models.
The model is fit for 10000 training epochs, which is arbitrary, as the model begins generating plausible items of clothing after perhaps 200 epochs. A batch size of 100 samples is used, batches of real and fake samples and updates to the model. The summarize_performance() function is called every 10 epochs,training steps.
For a given training step, first the discriminator model is updated for a half batch of real samples, then a half batch of fake samples, together forming one batch of weight updates. The generator is then updated via the combined GAN model. Importantly, the class label is set to 1, or real, for the fake samples. This has the effect of updating the generator toward getting better at generating real samples on the next batch.
The output images are saved as a jpeg format.

----------------------------------------------------------------------------------------------------------
# Recommending Similar Fashion Images with Deep Learning from scratch
We will train such neural networks to classify the clothing images into 51 categorical labels and use the feature layer as the deep features of the images. The feature layer will be able to capture features of the clothes, like the categories, fabrics, and patterns.
 
## ResNet Model
To classify the images, we use a model based on deep residual networks ResNet of Tensorflow.
 
## Training
We have preprocessed the image and resizes them to 64 x 64 dimension. We write a **prepare_df** function, which takes the path of a csv file and its column as inputs, and then returns a Pandas dataframe as output. We also write a **loss** function, which takes in labels, logits, bounding boxes and their labels as inputs, and returns a sum loss of cross entropy loss and mean squared error loss.
 
### Test Main Function
All the images are then evaluated/tested using the well-trained model. The nearest neighbor search is based on the values of the feature layer.
The output matrix is used to find the 2 best fitted classes among the all and output images will be displayed on the application.

---------------------------------------------------------------------------------------------------------
# Transfer Learning for Category and Style classification with Keras
 
In the third sub-part of our app we are using to instantiate the **InceptionV3** network as a transfer learning model. Then we add our custom classification layer, preserving the original Inception-v3 architecture but adapting the output to our 51 number of classes. Now we need to freeze all our `base_model` layers and train the last ones. An additional step can be performed after this initial training un-freezing some lower convolutional layers and retraining the classifier with a lower learning rate. This fine-tuning step increases the network accuracy but must be carefully carried out to avoid overfitting. Finally, we can train our custom classifier using the **Train()** method for transfer learning. In this example, it is going to take just a few minutes and five epochs to converge with a good accuracy.
