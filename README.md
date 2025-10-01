# SRGZara

SRGZara

* Kotlin used in all code
* Clean architecture
* MVVM


## Project modules

### domain

* Definition of business model, data models, repository's abstracts and usecases.

### data

* Abstract implementation of domain's layer.
* Repositories, datasources, net APIs, mappers, etc.

### app

* Dependency injection, main Android definitions.
* Main activity and fragments of the app, along with their viewModels.
* Common utilities and helpers that the app may need.
* Also the module where UI assets are kept - layouts, drawables, strings, etc.

## Main libraries/Jetpack/Android resources used

* Hilt for dependency injection
* Retrofit and Gson for network calling
* Material3 to keep up with default android looks
* Shimmer to show while the network call is loading
* JUnit, Mockito and Turbine for unit testing
* Coil for image loading.
* XMl, Compose and Navigation from Jetpack.


## App explanation

As per requirements the user can browse from the main list of characters along with some filtering.

### MainActivity

Initial definitions are done here, it is also where the navigation bottom bar is defined .

## HomeFragment

It is the main fragment that loads when the app opens. It loads the list of characters from the REST
API which will keep loading more data as the user scrolls down. The list will show a shimmer effect
when the network calls takes long, though an initial delay of 2 seconds it setup to display such
behaviour. On the top there is a search bar to filter characters by text. Additionally the user can 
perform a search online by name when the option is toggled in the [SettingsFragment]. All the
data is kept in cache and will load if no net is found if loaded previously, also works with images 
thanks to Coil. There are error snackbars that will show if some network error triggers. Upon 
clicking on one of the items of the list the user will be taken to [CharDetailFragment]. The data is
saved in a state so if the app gets killed due to a lack of recurses while in the background or
similar, it will be able to restore the previous status without making any additional call.

## SettingsFragment

This fragment shows some options that can be changed for the app. The filter toggle switches between
basic filtering and online search by name for the list in [HomeFragment]. The language buttons will
change the language of the app, keep in mind this will restart the app. The last option is to clear
the cache of the data stored previously.

## CharDetailFragment

This fragment shows information of a character. This view, along with the flow of navigation, will
be kept even if the app is killed while in the background.


## Other considerations

* Coroutines have been used to make network calls.
* [HomeFragment] is made with XML, [SettingsFragment] and [CharDetailFragment] is done with Compose.
* Taken in consideration the possibility of process of death for the home screen so the network call
  is not done again.
* There are unit tests of main classes that I consider the most important of the project.