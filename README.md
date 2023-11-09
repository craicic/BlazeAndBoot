# BlazeAndBoot

##### What is this repo ?
This is a training repo in order to learn how to integrate Spring Data JPA / Hibernate and Blaze Persistence in the same project.\
I have a sample DB with a classic usage of **image_blob** in a unidirectional one-to-one relationship with **image**. This is a 
relation that exists in my main project and I want to use Blaze's EntityView on this relation. 

##### The database diagram

```mermaid
classDiagram
direction BT
class image {
   integer post_id
   integer id
}
class image_blob {
   bytea content
   integer image_id
}
class post {
   varchar(255) body
   varchar(255) title
   integer id
}

image  -->  post : post_id=id
image_blob  -->  image : image_id=id
```