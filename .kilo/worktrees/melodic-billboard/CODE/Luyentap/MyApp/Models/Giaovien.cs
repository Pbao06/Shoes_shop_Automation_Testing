using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class GiaoVien
{
    [Key]
    [StringLength(4, ErrorMessage = "Mã giáo viên có chiều dài đúng 4 ký tự.")]
    [RegularExpression(@"^[A-Z]\d{2}[FM]$", ErrorMessage = "MaGV không đúng định dạng (1 chữ cái đầu mã khoa, 2 ký tự số kế tiếp, cuối là F hoặc M).")]
    public string MaGv { get; set; } = string.Empty;

    [Required(ErrorMessage = "Tên giáo viên không được để trống.")]
    public string TenGv { get; set; } = string.Empty;

    [Required(ErrorMessage = "Mã khoa không được để trống.")]
    public string MaKhoa { get; set; } = string.Empty;
    [ForeignKey("MaKhoa")]
    public virtual Khoa? Khoa { get; set; }

    public virtual ICollection<GiangDay> GiangDays { get; set; } = new List<GiangDay>();
}
