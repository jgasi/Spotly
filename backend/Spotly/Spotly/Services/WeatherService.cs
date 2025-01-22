using Spotly.DTOs;
using System.Text.Json;

namespace Spotly.Services
{
    public class WeatherService
    {
        private readonly HttpClient _httpClient;
        private readonly string _apiKey;
        private readonly string _baseUrl;
        private readonly string _location;

        public WeatherService(IConfiguration configuration, HttpClient httpClient)
        {
            _httpClient = httpClient;
            _apiKey = configuration["WeatherSettings:ApiKey"];
            _baseUrl = configuration["WeatherSettings:BaseUrl"];
            _location = configuration["WeatherSettings:Location"];
        }


        public async Task<WeatherResponse> GetWeatherAsync()
        {
            var endpoint = $"{_baseUrl}?q={_location}&appid={_apiKey}&units=metric";
            var response = await _httpClient.GetAsync(endpoint);

            if (!response.IsSuccessStatusCode)
            {
                return null;
            }

            var jsonResponse = await response.Content.ReadAsStringAsync();
            var weatherData = JsonSerializer.Deserialize<WeatherResponse>(jsonResponse);


            return weatherData;
        }
    }
}
