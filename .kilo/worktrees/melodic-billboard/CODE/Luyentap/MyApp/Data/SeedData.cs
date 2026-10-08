namespace Data
{
    public static class SeedData
    {
        public static void Initialize(AppDbContext db)
        {
            if (db.Khoas.Any())
            {
                return;
            }
            // 1. Thêm dữ liệu bảng KHOA
            var khoaCntt = new Khoa { MaKhoa = "CNTT", TenKhoa = "Công nghệ thông tin", SlCbgd = 3 };
            var khoaToan = new Khoa { MaKhoa = "TOAN", TenKhoa = "Toán", SlCbgd = 1 };
            var khoaSinh = new Khoa { MaKhoa = "SINH", TenKhoa = "Sinh học", SlCbgd = 0 };
            db.Khoas.AddRange(khoaCntt, khoaToan, khoaSinh);
            // 2. Thêm dữ liệu bảng SINHVIEN (dựa theo bảng mẫu trong hình đầu tiên của bạn)
            // 4. Seed bảng SINH VIÊN (nếu chưa có)
            if (!db.SinhViens.Any())
            {
                db.SinhViens.AddRange(
                    new SinhVien
                    {
                        Mssv = "C0001F",
                        Ten = "Bùi Thúy An",
                        PhaiNu = 1,
                        DiaChi = "223 Trần Hưng Đạo, HCM",
                        DienThoai = "38132202",
                        MaKhoa = "CNTT",
                        SoCmnd = "135792468",
                        NgaySinh = new DateTime(1992, 8, 14),
                        NgayNhapHoc = new DateTime(2010, 10, 1),
                        NgayRaTruong = new DateTime(2012, 11, 15),
                        LyDoNgungHoc = "Xin thôi học"
                    },
                    new SinhVien
                    {
                        Mssv = "C0002M",
                        Ten = "Nguyễn Thanh Tùng",
                        PhaiNu = 0,
                        DiaChi = "140 Cống Quỳnh, Sóc Trăng",
                        DienThoai = "38125678",
                        MaKhoa = "CNTT",
                        SoCmnd = "987654321",
                        NgaySinh = new DateTime(1992, 11, 23),
                        NgayNhapHoc = new DateTime(2010, 10, 1)
                    },
                    new SinhVien
                    {
                        Mssv = "T0003M",
                        Ten = "Nguyễn Thành Long",
                        PhaiNu = 0,
                        DiaChi = "112/4 Cống Quỳnh, HCM",
                        DienThoai = "0918345623",
                        MaKhoa = "TOAN",
                        SoCmnd = "123456789",
                        NgaySinh = new DateTime(1991, 8, 17),
                        NgayNhapHoc = new DateTime(2010, 10, 1),
                        NgayVaoDoan = new DateTime(2007, 5, 19),
                        NgayVaoDang = new DateTime(2012, 5, 1)
                    },
                    // --- THÊM 2 BẠN CÒN LẠI VÀO ĐÂY ---
                    new SinhVien
                    {
                        Mssv = "C0004F", // Thay đổi Mã số sinh viên tương ứng với đề của bạn
                        Ten = "Tên Bạn Thứ 4",
                        PhaiNu = 1,
                        DiaChi = "Địa chỉ bạn thứ 4",
                        DienThoai = "Số điện thoại",
                        MaKhoa = "CNTT",
                        SoCmnd = "Số CMND",
                        NgaySinh = new DateTime(1992, 1, 1),
                        NgayNhapHoc = new DateTime(2010, 10, 1)
                    },
                    new SinhVien
                    {
                        Mssv = "T0005M", // Thay đổi Mã số sinh viên tương ứng với đề của bạn
                        Ten = "Tên Bạn Thứ 5",
                        PhaiNu = 0,
                        DiaChi = "Địa chỉ bạn thứ 5",
                        DienThoai = "Số điện thoại",
                        MaKhoa = "TOAN",
                        SoCmnd = "Số CMND",
                        NgaySinh = new DateTime(1992, 1, 1),
                        NgayNhapHoc = new DateTime(2010, 10, 1)
                    }
                );
                db.SaveChanges();
            }
            // 2. Seed bảng MÔN HỌC (nếu chưa có)
            if (!db.MonHocs.Any())
            {
                db.MonHocs.AddRange(
                    new MonHoc { MaMh = "CSDL", TenMh = "Cơ sở dữ liệu", SotcToiThieu = 2 },
                    new MonHoc { MaMh = "CTDL", TenMh = "Cấu trúc dữ liệu", SotcToiThieu = 3 },
                    new MonHoc { MaMh = "KTLT", TenMh = "Kỹ thuật lập trình", SotcToiThieu = 3 },
                    new MonHoc { MaMh = "CWIN", TenMh = "Lập trình C trên Windows", SotcToiThieu = 3 },
                    new MonHoc { MaMh = "TRR", TenMh = "Toán rời rạc", SotcToiThieu = 2 },
                    new MonHoc { MaMh = "LTDT", TenMh = "Lý thuyết đồ thị", SotcToiThieu = 2 }
                );
                db.SaveChanges();
            }
            // 3. Seed bảng GIÁO VIÊN (dựa theo bảng mẫu trong hình giáo viên của bạn)
            if (!db.GiaoViens.Any())
            {
                db.GiaoViens.AddRange(
                    new GiaoVien { MaGv = "C01F", TenGv = "Phạm Thị Thảo", MaKhoa = "CNTT" },
                    new GiaoVien { MaGv = "T02M", TenGv = "Lâm Hoàng Vũ", MaKhoa = "TOAN" },
                    new GiaoVien { MaGv = "C03M", TenGv = "Trần Văn Tiến", MaKhoa = "CNTT" },
                    new GiaoVien { MaGv = "C04M", TenGv = "Hoàng Vương", MaKhoa = "CNTT" },
                    new GiaoVien { MaGv = "T05F", TenGv = "Thanh Trà", MaKhoa = "TOAN" }
                );
                db.SaveChanges();
            }
            // 5. Seed bảng GIẢNG DẠY (nếu chưa có)
            if (!db.GiangDays.Any())
            {
                db.GiangDays.AddRange(
                    new GiangDay { MaKhoaHoc = "K001", MaGv = "C01F", MaMh = "CSDL", HocKy = 1, NienKhoa = "2010-2011", TongSoTc = 3, Sotclt = 2, Sotcth = 1, SoTietLt = 30, SoTietTh = 15 },
                    new GiangDay { MaKhoaHoc = "K002", MaGv = "C03M", MaMh = "CTDL", HocKy = 2, NienKhoa = "2010-2011", TongSoTc = 3, Sotclt = 2, Sotcth = 1, SoTietLt = 30, SoTietTh = 15 },
                    new GiangDay { MaKhoaHoc = "K003", MaGv = "T02M", MaMh = "TRR", HocKy = 1, NienKhoa = "2011-2012", TongSoTc = 2, Sotclt = 2, Sotcth = 0, SoTietLt = 30, SoTietTh = 0 },
                    new GiangDay { MaKhoaHoc = "K004", MaGv = "C04M", MaMh = "KTLT", HocKy = 2, NienKhoa = "2011-2012", TongSoTc = 3, Sotclt = 2, Sotcth = 1, SoTietLt = 30, SoTietTh = 15 },
                    new GiangDay { MaKhoaHoc = "K005", MaGv = "T05F", MaMh = "LTDT", HocKy = 1, NienKhoa = "2012-2013", TongSoTc = 2, Sotclt = 2, Sotcth = 0, SoTietLt = 30, SoTietTh = 0 }
                );
                db.SaveChanges();
            }
            // 6. Seed bảng KẾT QUẢ (dựa theo bảng mẫu kết quả của bạn)
            if (!db.KetQuas.Any())
            {
                db.KetQuas.AddRange(
                    new KetQua { Mssv = "C0001F", MaKhoaHoc = "K001", DiemKtGiuaKy = 8.5, DiemThiLan1 = 5, DiemThiLan2 = null },
                    new KetQua { Mssv = "C0001F", MaKhoaHoc = "K003", DiemKtGiuaKy = 8.0, DiemThiLan1 = 9, DiemThiLan2 = null },
                    new KetQua { Mssv = "T0003M", MaKhoaHoc = "K004", DiemKtGiuaKy = 9.0, DiemThiLan1 = 7, DiemThiLan2 = null },
                    new KetQua { Mssv = "C0001F", MaKhoaHoc = "K002", DiemKtGiuaKy = 9.0, DiemThiLan1 = 7, DiemThiLan2 = null },
                    new KetQua { Mssv = "T0003M", MaKhoaHoc = "K003", DiemKtGiuaKy = 6.0, DiemThiLan1 = 2, DiemThiLan2 = 2.5 },
                    new KetQua { Mssv = "T0005M", MaKhoaHoc = "K003", DiemKtGiuaKy = 9.0, DiemThiLan1 = 7, DiemThiLan2 = null },
                    new KetQua { Mssv = "C0002M", MaKhoaHoc = "K001", DiemKtGiuaKy = 7.0, DiemThiLan1 = 2, DiemThiLan2 = 5 },
                    new KetQua { Mssv = "T0003M", MaKhoaHoc = "K002", DiemKtGiuaKy = 6.5, DiemThiLan1 = 2, DiemThiLan2 = 3 },
                    new KetQua { Mssv = "T0005M", MaKhoaHoc = "K005", DiemKtGiuaKy = 7.0, DiemThiLan1 = 10, DiemThiLan2 = null },
                    new KetQua { Mssv = "C0001F", MaKhoaHoc = "K004", DiemKtGiuaKy = 8.0, DiemThiLan1 = 9, DiemThiLan2 = null }
                );
                db.SaveChanges();
            }

            // Lưu toàn bộ thay đổi xuống database MySQL
            db.SaveChanges();
            Console.WriteLine("Đã seed dữ liệu mẫu thành công từ file SeedData!");





        }
    }
}