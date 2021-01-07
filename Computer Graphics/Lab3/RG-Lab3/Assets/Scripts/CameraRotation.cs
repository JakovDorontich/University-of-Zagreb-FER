using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraRotation : MonoBehaviour
{
    public Camera mainCamera;
    public float speed = 2.5f;

    public float minFoV = 30.0f;
    public float maxFoV = 80.0f;
    public float sensitivity = 10.0f;

    private void Start()
    {
        mainCamera.transform.LookAt(Vector3.zero);
    }

    private void Update()
    {
        mainCamera.transform.RotateAround(Vector3.zero, Vector3.up, 20 * Time.deltaTime * speed);

        float cameraFoV = Camera.main.fieldOfView;
        cameraFoV += Input.GetAxis("Mouse ScrollWheel") * -sensitivity;
        cameraFoV = Mathf.Clamp(cameraFoV, minFoV, maxFoV);
        Camera.main.fieldOfView = cameraFoV;
    }
}
