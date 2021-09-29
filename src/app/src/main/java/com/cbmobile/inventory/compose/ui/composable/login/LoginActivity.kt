package com.cbmobile.inventory.compose.ui.composable.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.cbmobile.inventory.compose.InventoryApplication
import com.cbmobile.inventory.compose.R
import com.cbmobile.inventory.compose.ui.composable.MainActivity

import com.cbmobile.inventory.compose.ui.theme.InventoryTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as InventoryApplication).container
        setContent {
            InventoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val viewModel = LoginViewModel(appContainer.authenticationService)
                    SetupLogin(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SetupLogin(viewModel: LoginViewModel){
    val username = viewModel.username.observeAsState("")
    val password =  viewModel.password.observeAsState("")
    val isError = viewModel.isError.observeAsState(false)
    val context = LocalContext.current

    val onLoginCheck: () -> Unit = {
        if (viewModel.login()){
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    Login(username = username.value,
        password = password.value,
        isLoginError = isError.value,
        onUsernameChanged = viewModel.onUsernameChanged,
        onPasswordChanged = viewModel.onPasswordChanged,
        login = onLoginCheck)
}

@Composable
fun Login(
    username: String,
    password: String,
    isLoginError: Boolean,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    login: () -> Unit) {

    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Image (
            rememberDrawablePainter(drawable = ContextCompat.getDrawable(context, R.drawable.acmelogo)),
            contentDescription = "Logo",
            modifier = Modifier.padding(bottom = 32.dp)
        ) 
        OutlinedTextField(value = username,
            onValueChange = { onUsernameChanged(it) },
            label = { Text("username") },
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(modifier =  Modifier.padding(top = 16.dp),
            value = password,
            onValueChange = { onPasswordChanged(it) },
            label = { Text("password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                login()
            })
        )
        Button(modifier = Modifier.padding(top = 32.dp),
            onClick = {
                login()
            })
        {
            Text("Login", style = MaterialTheme.typography.h5)
        }
        if (isLoginError) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.error,
                text = "Error: username or password is incorrect"
            )
        }
   }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val username = ""
    val password = ""
    val isError = false

    InventoryTheme {
        Login(username = username,
            password = password,
            isLoginError = isError,
            onUsernameChanged = { },
            onPasswordChanged = { },
            login = { } )
    }
}