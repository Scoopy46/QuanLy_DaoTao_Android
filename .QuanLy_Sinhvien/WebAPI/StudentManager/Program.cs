using Microsoft.EntityFrameworkCore;
using StudentManager.Data;
using StudentManager.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Cấu hình CORS để cho phép Android app kết nối
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAndroidApp", policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

builder.Services.AddDbContext<SinhVienDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("SinhVienDb")));

builder.Services.AddScoped<ISinhVienService, SinhVienService>();
builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<IDiemThiService, DiemThiService>();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Bỏ UseHttpsRedirection để cho phép HTTP
// app.UseHttpsRedirection();

// Sử dụng CORS
app.UseCors("AllowAndroidApp");

app.UseAuthorization();
app.MapControllers();
app.Run();