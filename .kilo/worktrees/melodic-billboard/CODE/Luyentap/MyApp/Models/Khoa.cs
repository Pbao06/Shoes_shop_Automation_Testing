using System.ComponentModel.DataAnnotations;

public class Khoa
{
    [Key]
    [Required(ErrorMessage = "Mã khoa không được để trống.")]
    public string MaKhoa { get; set; } = string.Empty;

    [Required(ErrorMessage = "Tên khoa không được để trống.")]
    public string TenKhoa { get; set; } = string.Empty;

    [Range(0, 4, ErrorMessage = "Số lượng cán bộ giảng dạy (SL_CBGD) phải nằm trong khoảng từ 0 đến 4.")]
    public int SlCbgd { get; set; } = 0; // Mặc định là 0
    public virtual ICollection<SinhVien> SinhViens { get; set; } = new List<SinhVien>();
    public virtual ICollection<GiaoVien> GiaoViens { get; set; } = new List<GiaoVien>();
}