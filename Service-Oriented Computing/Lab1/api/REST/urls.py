from django.urls import path, include
from . import views
#from rest_framework import routers
from django.conf.urls.static import static
from django.conf import settings
from rest_framework_nested import routers


router = routers.DefaultRouter()
router.register('photos', views.PhotoView)
router.register('users', views.UserView)

domains_router = routers.NestedDefaultRouter(router, 'users', lookup='user')
domains_router.register('photos', views.PhotoViewNasted, base_name='photos')

urlpatterns = [
    path('', include(router.urls)),
    path('', include(domains_router.urls)),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
