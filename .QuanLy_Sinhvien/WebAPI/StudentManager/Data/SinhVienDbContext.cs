using Microsoft.EntityFrameworkCore;
using StudentManager.Models;

namespace StudentManager.Data
{
    public class SinhVienDbContext : DbContext
    {
        public SinhVienDbContext(DbContextOptions<SinhVienDbContext> options) : base(options) { }

        public DbSet<SinhVien> SinhVien { get; set; }
        public DbSet<User> User { get; set; }
        public DbSet<MonHoc> MonHoc { get; set; }
        public DbSet<DiemThi> DiemThi { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<SinhVien>()
                .HasKey(s => s.MaSV);

            modelBuilder.Entity<SinhVien>()
                .Property(s => s.Anh)
                .HasDefaultValue("/storage/DCIM/default.jpg");

            modelBuilder.Entity<User>()
                .HasIndex(u => u.UserName)
                .IsUnique();

            modelBuilder.Entity<MonHoc>()
                .HasKey(m => m.MaMH);

            modelBuilder.Entity<DiemThi>()
                .HasKey(d => new { d.MaSV, d.MaMH });

            modelBuilder.Entity<DiemThi>()
                .HasOne(d => d.SinhVien)
                .WithMany()
                .HasForeignKey(d => d.MaSV)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<DiemThi>()
                .HasOne(d => d.MonHoc)
                .WithMany()
                .HasForeignKey(d => d.MaMH)
                .OnDelete(DeleteBehavior.Restrict);
        }
    }
}