using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class UiController : MonoBehaviour
{
    public GameObject[] models;
    public Material[] materials;

    private List<MeshRenderer> objectMeshRenderers;

    private void Start()
    {
        objectMeshRenderers = new List<MeshRenderer>();
        foreach (GameObject model in models)
        {
            MeshRenderer meshRenderer = model.GetComponent<MeshRenderer>();
            meshRenderer.material = materials[0];
            objectMeshRenderers.Add(meshRenderer);
            model.SetActive(false);
        }
        models[0].SetActive(true);

    }

    private void Update()
    {
        if (Input.GetKey("escape"))
        {
            Application.Quit();
        }

    }

    public void ChoseObject(string objectName)
    {
        foreach(GameObject model in models)
        {
            model.SetActive(false);
        }

        switch (objectName.ToLower())
        {
            case "sphere":
                models[0].SetActive(true);
                break;
            case "cube":
                models[1].SetActive(true);
                break;
            case "plane":
                models[2].SetActive(true);
                break;
            case "teapot":
                models[3].SetActive(true);
                break;
            case "fer":
                models[4].SetActive(true);
                break;
        }
    }

    public void ChoseMaterial(string materialName)
    {
        switch (materialName.ToLower())
        {
            case "shader1":
                SetMaterialAll(materials[0]);
                break;
            case "shader2":
                SetMaterialAll(materials[1]);
                break;
            case "shader3":
                SetMaterialAll(materials[2]);
                break;
            case "shader4":
                SetMaterialAll(materials[3]);
                break;
            case "shader5":
                SetMaterialAll(materials[4]);
                break;
        }
    }

    private void SetMaterialAll(Material material)
    {
        foreach(MeshRenderer meshRenderer in objectMeshRenderers)
        {
            meshRenderer.material = material;
        }
    }

    public void ExitProject()
    {
        Application.Quit();
    }
}
