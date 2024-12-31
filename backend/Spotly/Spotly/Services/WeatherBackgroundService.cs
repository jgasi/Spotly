
namespace Spotly.Services
{
    public class WeatherBackgroundService : BackgroundService
    {
        private readonly WeatherService _weatherService;

        public WeatherBackgroundService(WeatherService weatherService)
        {
            _weatherService = weatherService;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                try
                {
                    var weather = await _weatherService.GetWeatherAsync();
                    Console.WriteLine($"Temperature: {weather.Weather[0].Main}, Condition: {weather.Weather[0].Description}");
                    if (weather.Weather[0].Main == "Snow" || weather.Weather[0].Main == "Thunderstorm")
                    {
                        //TODO: Send notification
                    }
                } catch (Exception ex)
                {
                    Console.WriteLine($"Error fetching weather: {ex.Message}");
                }

                await Task.Delay(TimeSpan.FromMinutes(1), stoppingToken);
            }
        }
    }
}
