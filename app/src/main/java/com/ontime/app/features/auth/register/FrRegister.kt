package com.ontime.app.features.auth.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.ontime.app.activities.MainActivity
import com.ontime.app.R
import kotlinx.android.synthetic.main.fr_register.*
import kotlinx.android.synthetic.main.fr_register.editEmail
import kotlinx.android.synthetic.main.fr_register.editPassword
import kotlinx.android.synthetic.main.fr_register.view.*

class FrRegister : Fragment() {

    /*ver si se puede simplificar los metodos oncreate, oncreateview, onviewcreated, onactivityresult*/
    /*agregar comentarios de cada funcion*/
    lateinit var vmRegister: VmRegister
    private val callbackManager = CallbackManager.Factory.create()
    lateinit var v: View
    private val prefName = "myPreferences"

    companion object {
        fun newInstance() = FrRegister()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmRegister = ViewModelProviders.of(this).get(VmRegister::class.java)
        vmRegister.userMutableLiveData.observe(this,
            { firebaseUser: FirebaseUser? ->
                if (firebaseUser != null) {
                    Toast.makeText(activity, "Login successfull", Toast.LENGTH_SHORT).show()
                    /*Colocar en una funcion, las funcionalidades de sharedPreferences*/
                    firebaseUser?.let {
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val uid = firebaseUser.uid
                        Log.d("Este es el user", email.toString())
                        val sharedPref: SharedPreferences =
                            requireContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("UID", uid)
                        editor.putString("NAME", name)
                        editor.putString("EMAIL", email)

                        editor.apply()
                    }


                    Navigation.findNavController(v)
                        .navigate(R.id.action_registerFragment_to_comerceRegisterFragment)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fr_register, container, false)
        v.btnGoToLogin.setOnClickListener {
            Navigation.findNavController(v)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*Colocar el choclo en variables y pasarlas como parametro en la funcion*/
        btnRegister.setOnClickListener {
            if (editEmail.text.trim().toString().isNotEmpty() || editPassword.text.trim().toString()
                    .isNotEmpty()
            ) {
                vmRegister.register(
                    editEmail.text.trim().toString(),
                    editPassword.text.trim().toString()
                )

            } else {
                Toast.makeText(activity, "Input required", Toast.LENGTH_LONG).show()
            }
        }

        btnRegisterGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient((activity as MainActivity), gso)
            startActivityForResult(googleClient.signInIntent, 100)
        }

        btnRegisterFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result?.let {
                            val token = it.accessToken
                            vmRegister.loginWithFacebook(token)
                        }
                    }

                    override fun onCancel() {
                        Log.w("Task message", "facebook:cancel")
                    }

                    override fun onError(error: FacebookException?) {
                        Log.w("Task message", "facebook:failure", error)
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Task message", "firebaseAuthWithGoogle:" + account.id)
                vmRegister.loginWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Task message", "Google sign in failed", e)
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

}