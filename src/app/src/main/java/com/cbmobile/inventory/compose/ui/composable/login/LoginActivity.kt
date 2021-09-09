package com.cbmobile.inventory.compose.ui.composable.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmobile.inventory.compose.R
import com.cbmobile.inventory.compose.ui.composable.MainActivity

import com.cbmobile.inventory.compose.ui.theme.InventoryTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background,
                        modifier = Modifier.fillMaxSize()) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Login(viewModel: LoginViewModel = LoginViewModel()) {
    var username = viewModel.username.observeAsState("")
    var password = viewModel.password.observeAsState("")
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Image (
            painter = painterResource(id = R.drawable.acmelogo),
            contentDescription = "Logo",
            modifier = Modifier.padding(bottom = 32.dp)
        ) 
        OutlinedTextField(value = username.value,
            onValueChange = { viewModel.onUsernameChanged(it) },
            label = { Text("username") }
        )
        OutlinedTextField(modifier =  Modifier.padding(top = 16.dp),
            value = password.value,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(modifier = Modifier.padding(top = 32.dp),
            onClick = {
                if (viewModel.login()){
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    //todo-show error that they have wrong password
                }

            })
        {
            Text("Login", style = MaterialTheme.typography.h5)
        }
   }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InventoryTheme {
        Login()
    }
}