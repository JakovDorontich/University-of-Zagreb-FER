from django.db import models

class User(models.Model):
    name = models.CharField(max_length=50)

    def __str__(self):
        return self.name

class Photo(models.Model):
    photo = models.ImageField("Upload photo")
    user_id = models.ForeignKey(User, related_name='photos', on_delete=models.CASCADE)

