## Weather App for RBC Interview
This app is an Android weather app based of the Open Weather API. This API is publicly available
here https://openweathermap.org/api/one-call-3.
The app is comprised of a main screen and a detail screen. The main screen has a view of the current
weather, an hourly forecast, and a weekly forecast. The detail screen shows more information about
each day on the weekly forecast.

The app is architected using UDF principles with a UI layer, a logic layer, and a repository layer.

The UI layer is using Jetpack Compose, alongside with compose navigation. The navigation graph is
maintained on a single activity, which greatly simplifies lifecycle management.

The logic layer is comprised of view models. The view models are controlled by Hilt, allowing for
the repositories and navigation bundles to be injected directly. The hiltViewModel() hooks work
particularly well with Compose, as they can inject Compose navigation bundles automatically.

The repository layer is a single Retrofit repository which is managed and injected by Hilt. It is
kept as a singleton on the top level and can be easily expanded to include more queries.

## Art Sources:
https://www.svgrepo.com/collection/weather-33
