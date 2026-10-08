using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace MyApp.Migrations
{
    /// <inheritdoc />
    public partial class Init : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterDatabase()
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "Khoas",
                columns: table => new
                {
                    MaKhoa = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    TenKhoa = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    SlCbgd = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Khoas", x => x.MaKhoa);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "MonHocs",
                columns: table => new
                {
                    MaMh = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    TenMh = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    SotcToiThieu = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_MonHocs", x => x.MaMh);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "GiaoViens",
                columns: table => new
                {
                    MaGv = table.Column<string>(type: "varchar(4)", maxLength: 4, nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    TenGv = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    MaKhoa = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_GiaoViens", x => x.MaGv);
                    table.ForeignKey(
                        name: "FK_GiaoViens_Khoas_MaKhoa",
                        column: x => x.MaKhoa,
                        principalTable: "Khoas",
                        principalColumn: "MaKhoa",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "SinhViens",
                columns: table => new
                {
                    Mssv = table.Column<string>(type: "varchar(6)", maxLength: 6, nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    Ten = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    PhaiNu = table.Column<int>(type: "int", nullable: false),
                    DiaChi = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    DienThoai = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    MaKhoa = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    SoCmnd = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    NgaySinh = table.Column<DateTime>(type: "datetime(6)", nullable: true),
                    NgayNhapHoc = table.Column<DateTime>(type: "datetime(6)", nullable: false),
                    NgayVaoDoan = table.Column<DateTime>(type: "datetime(6)", nullable: true),
                    NgayVaoDang = table.Column<DateTime>(type: "datetime(6)", nullable: true),
                    NgayRaTruong = table.Column<DateTime>(type: "datetime(6)", nullable: true),
                    LyDoNgungHoc = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_SinhViens", x => x.Mssv);
                    table.ForeignKey(
                        name: "FK_SinhViens_Khoas_MaKhoa",
                        column: x => x.MaKhoa,
                        principalTable: "Khoas",
                        principalColumn: "MaKhoa",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "GiangDays",
                columns: table => new
                {
                    MaKhoaHoc = table.Column<string>(type: "varchar(4)", maxLength: 4, nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    MaGv = table.Column<string>(type: "varchar(4)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    MaMh = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    HocKy = table.Column<int>(type: "int", nullable: false),
                    NienKhoa = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    NgayBatDauLyThuyet = table.Column<DateTime>(type: "datetime(6)", nullable: true),
                    NgayBatDauThuHanh = table.Column<DateTime>(type: "datetime(6)", nullable: true),
                    NgayKetThuc = table.Column<DateTime>(type: "datetime(6)", nullable: true),
                    TongSoTc = table.Column<int>(type: "int", nullable: false),
                    Sotclt = table.Column<int>(type: "int", nullable: false),
                    Sotcth = table.Column<int>(type: "int", nullable: false),
                    SoTietLt = table.Column<int>(type: "int", nullable: false),
                    SoTietTh = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_GiangDays", x => x.MaKhoaHoc);
                    table.ForeignKey(
                        name: "FK_GiangDays_GiaoViens_MaGv",
                        column: x => x.MaGv,
                        principalTable: "GiaoViens",
                        principalColumn: "MaGv",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_GiangDays_MonHocs_MaMh",
                        column: x => x.MaMh,
                        principalTable: "MonHocs",
                        principalColumn: "MaMh",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "KetQuas",
                columns: table => new
                {
                    Mssv = table.Column<string>(type: "varchar(6)", maxLength: 6, nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    MaKhoaHoc = table.Column<string>(type: "varchar(4)", maxLength: 4, nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    DiemKtGiuaKy = table.Column<double>(type: "double", nullable: false),
                    DiemThiLan1 = table.Column<double>(type: "double", nullable: false),
                    DiemThiLan2 = table.Column<double>(type: "double", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_KetQuas", x => new { x.Mssv, x.MaKhoaHoc });
                    table.ForeignKey(
                        name: "FK_KetQuas_GiangDays_MaKhoaHoc",
                        column: x => x.MaKhoaHoc,
                        principalTable: "GiangDays",
                        principalColumn: "MaKhoaHoc",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_KetQuas_SinhViens_Mssv",
                        column: x => x.Mssv,
                        principalTable: "SinhViens",
                        principalColumn: "Mssv",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_GiangDays_MaGv",
                table: "GiangDays",
                column: "MaGv");

            migrationBuilder.CreateIndex(
                name: "IX_GiangDays_MaMh",
                table: "GiangDays",
                column: "MaMh");

            migrationBuilder.CreateIndex(
                name: "IX_GiaoViens_MaKhoa",
                table: "GiaoViens",
                column: "MaKhoa");

            migrationBuilder.CreateIndex(
                name: "IX_KetQuas_MaKhoaHoc",
                table: "KetQuas",
                column: "MaKhoaHoc");

            migrationBuilder.CreateIndex(
                name: "IX_SinhViens_MaKhoa",
                table: "SinhViens",
                column: "MaKhoa");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "KetQuas");

            migrationBuilder.DropTable(
                name: "GiangDays");

            migrationBuilder.DropTable(
                name: "SinhViens");

            migrationBuilder.DropTable(
                name: "GiaoViens");

            migrationBuilder.DropTable(
                name: "MonHocs");

            migrationBuilder.DropTable(
                name: "Khoas");
        }
    }
}
