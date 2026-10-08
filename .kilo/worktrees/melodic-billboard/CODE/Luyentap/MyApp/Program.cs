using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Data;

public class Program
{
    static void Main(string[] args)
    {
        Console.OutputEncoding = System.Text.Encoding.UTF8;

        var services = new ServiceCollection();
        // dang khi cac dich vu 

        services.AddDbContext<AppDbContext>((serviceProvider, options) =>
        {
            string connectionString = "Server=127.0.0.1;Port=3306;Database=managestudent;Uid=root;Pwd=652006;SslMode=None;AllowPublicKeyRetrieval=True;";
            var serverVersion = ServerVersion.AutoDetect(connectionString);
            options.UseMySql(connectionString, serverVersion);
        });

        services.AddScoped<LuyenTap>(); // dang ki DI 

        using var serviceProvider = services.BuildServiceProvider(); // build for DI , 
        using var scope = serviceProvider.CreateScope(); // create a bag for scoped 
        var db = scope.ServiceProvider.GetRequiredService<AppDbContext>(); // lay gia tri ra ne 
        var luyentapService=scope.ServiceProvider.GetRequiredService<LuyenTap>();

        
        luyentapService.Cau1();





    }
}