import json, os
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APITestCase
from .models import Photo, User


class Tests(APITestCase):

    def test_create_user(self):
        url = "/users/"
        data = {"name": "Euler"}

        response = self.client.post(url, data, format="json")
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(User.objects.count(), 1)
        self.assertEqual(User.objects.get().name, "Euler")


    def test_get_users(self):
        url = "/users/"
        data = {"name": "Euler"}

        response = self.client.post(url, data, format="json")
        response = self.client.get(url)
        self.assertEqual(response.status_code, status.HTTP_200_OK)


    def test_update_user(self):
        url = "/users/"
        data = {"name": "Euler"}
        url_updated = "/users/1/"
        data_updated = {"name": "Laplace"}

        response = self.client.post(url, data, format="json")
        response = self.client.put(url_updated, data_updated, format="json")
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(User.objects.get().name, "Laplace")

    
    def test_create_picture(self):
        url_user = "/users/"
        data_user = {"name": "Euler"}
        url_photo = "/photos/"
        data_photo = {"photo": "test_slika.jpg", "user_id": 1}

        response = self.client.post(url_user, data_user, format="json")
        response = self.client.post(url_photo, data_photo, format="json")


    def test_delete_user(self):
        url = "/users/"
        data = {"name": "Euler"}
        url_delete = "/users/1/"

        response = self.client.post(url, data, format="json")
        response = self.client.delete(url_delete, format="json")
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
        self.assertEqual(User.objects.count(), 0)

        

