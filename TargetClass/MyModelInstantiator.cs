MyModelInstantiator (C#)
using UnityEngine;
using System.Collections;
using Vuforia;

public class MyModelInstantiator: MonoBehaviour,
ITrackableEventHandler {
	private TrackableBehaviour mTrackableBehaviour;
	public Transform pokemonPrefab;
	//private AndroidJavaObject jo;
	private int intentos = 1;
	private bool atrapado;
	private string nombre;
	// Se crea el transform que corresponda a la imagen adecuada
	void Start() {
		mTrackableBehaviour = GetComponent < TrackableBehaviour > ();
		if (mTrackableBehaviour) {
			mTrackableBehaviour.RegisterTrackableEventHandler(this);
		}
		/*Se recoge la instancia de la clase con el campo del nombre, para poder crear
		 el bicho adecuado*/
		AndroidJavaClass jc = new AndroidJavaClass("com.AR.KisHCaptura.UnityPlayerNativeActivity");
		AndroidJavaObject jo = jc.GetStatic < AndroidJavaObject > ("activity");
		nombre = jo.Get < string > ("nombreGenerico");
		//Debug.Log ("el nombre es: "+nombre);
		//se instancia el transform con el nombre recibido
		pokemonPrefab = (Instantiate(Resources.Load(nombre)) as GameObject).transform;
		//pokemonPrefab = (Instantiate(Resources.Load("eevee")) as GameObject).transform;
	}
	// Update is called once per frame
	void Update() {
		if (Input.GetKeyUp(KeyCode.Escape)) {
			//Application.Quit();
			//Debug.Log ("onResume Received");
			AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
			AndroidJavaObject jo = jc.GetStatic < AndroidJavaObject > ("currentActivity");
			jo.Call("onBackPressed");
		}
	}
	public void OnTrackableStateChanged(TrackableBehaviour.Status previousStatus,
	TrackableBehaviour.Status newStatus) {
		if (newStatus == TrackableBehaviour.Status.DETECTED || newStatus == TrackableBehaviour.Status.TRACKED || newStatus == TrackableBehaviour.Status.EXTENDED_TRACKED) {
			OnTrackingFound();
		}
	}
	private void OnTrackingFound() {
		if (pokemonPrefab != null) {
			Debug.Log("Se encuentra");
			Transform myModelTrf = GameObject.Instantiate(pokemonPrefab) as Transform;
			myModelTrf.parent = mTrackableBehaviour.transform;
			myModelTrf.localPosition = new Vector3(0.00f, 0.00f, 0.00f);
			//myModelTrf.localRotation = Quaternion.identity;
			myModelTrf.localRotation = Quaternion.Euler(new Vector3(270, 180, 0));
			myModelTrf.localScale = new Vector3(0.2f, 0.2f, 0.2f);
			myModelTrf.gameObject.SetActive(true);
			AudioSource sonido = myModelTrf.audio;
			/*se hace sonar el grito*/
			if (sonido != null) {
				Debug.Log("sonido " + sonido);
				sonido.PlayOneShot(sonido.clip);
			}

		} else {
			Debug.Log("No se encuentra");
		}
	}
	void OnGUI() {
		GUIStyle gs = new GUIStyle(GUI.skin.button);
		gs.fontSize = 45;
		if (GUI.Button(new Rect(Screen.width * 13 / 100, Screen.height * 90 / 100, Screen.width * 75 / 100, Screen.height * 10 / 100), "Atrapalo", gs)) {
			atrapar();
		}
	}

	void atrapar() {
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject jo = jc.GetStatic < AndroidJavaObject > ("currentActivity");
		float conseguido = Random.value;
		if (intentos <= 3) {
			intentos++;
			if (conseguido > 0.5) {
				jo.Call("atrapado", bool.TrueString);
				//Debug.Log ("Conseguido");
			} else {
				//GUI.Label (Rect (25, 25, 100, 30), nombre+" ha esquivado tu PokeBall, prueba de nuevo");
			}
		} else {
			jo.Call("atrapado", bool.FalseString);
			//Debug.Log ("No conseguido");
		}
	}
}
