package com.example.myjetpackapplication.di



import com.example.data.di.ApolloClient
import com.example.data.di.DispatchersModule
import com.example.data.module.DataModule
import com.example.myjetpackapplication.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
               ApolloClient::class, DataModule::class,DispatchersModule::class
    ]
)

interface AppComponent {
    fun inject(application: com.example.myjetpackapplication.BaseApplication)
    fun inject(activity: MainActivity)
}
