from rest_framework import serializers
from .models import Photo, User

class PhotoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Photo
        fields = ('id', 'photo', 'user_id')

class UserSerializer(serializers.HyperlinkedModelSerializer):
    photos = serializers.HyperlinkedRelatedField(many=True, read_only=True, view_name='photo-detail')
    class Meta:
        model = User
        fields = ('id', 'name', 'photos')

