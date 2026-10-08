using Microsoft.EntityFrameworkCore;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options)
        : base(options)
    {
    }

    public DbSet<Khoa> Khoas { get; set; } = null!;
    public DbSet<SinhVien> SinhViens { get; set; } = null!;
    public DbSet<GiaoVien> GiaoViens { get; set; } = null!;
    public DbSet<MonHoc> MonHocs { get; set; } = null!;
    public DbSet<GiangDay> GiangDays { get; set; } = null!;
    public DbSet<KetQua> KetQuas { get; set; } = null!;

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        if (!optionsBuilder.IsConfigured)
        {
            string connectionString = "Server=127.0.0.1;Port=3306;Database=managestudent;Uid=root;Pwd=652006;SslMode=None;AllowPublicKeyRetrieval=True;";
            var serverVersion = ServerVersion.AutoDetect(connectionString);
            optionsBuilder.UseMySql(connectionString, serverVersion);
        }
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<KetQua>()
            .HasKey(k => new { k.Mssv, k.MaKhoaHoc });

        modelBuilder.Entity<KetQua>()
            .HasOne(k => k.SinhVien)
            .WithMany(s => s.KetQuas)
            .HasForeignKey(k => k.Mssv);

        modelBuilder.Entity<KetQua>()
            .HasOne(k => k.GiangDay)
            .WithMany(g => g.KetQuas)
            .HasForeignKey(k => k.MaKhoaHoc);
    }
}