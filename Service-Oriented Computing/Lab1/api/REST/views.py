from django.shortcuts import render, redirect
from rest_framework import viewsets, permissions
from .models import Photo, User
from .serializers import PhotoSerializer, UserSerializer
from rest_framework.response import Response


class PhotoView(viewsets.ModelViewSet):
    queryset = Photo.objects.all()
    serializer_class = PhotoSerializer

class UserView(viewsets.ModelViewSet):

    queryset = User.objects.all()
    serializer_class = UserSerializer

class PhotoViewNasted(viewsets.ModelViewSet):
    serializer_class = PhotoSerializer
    def get_queryset(self):
        return Photo.objects.filter(user_id=self.kwargs['user_pk'])



