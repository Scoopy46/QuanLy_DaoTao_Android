using System.ComponentModel.DataAnnotations;

namespace StudentManager.DTOs
{
    public class DiemThiDTO
    {
        [Required(ErrorMessage = "Mã sinh viên là bắt buộc")]
        [StringLength(10, ErrorMessage = "Mã sinh viên không được vượt quá 10 ký tự")]
        public string MaSV { get; set; } = string.Empty;

        [Required(ErrorMessage = "Mã môn học là bắt buộc")]
        [StringLength(10, ErrorMessage = "Mã môn học không được vượt quá 10 ký tự")]
        public string MaMH { get; set; } = string.Empty;

        [Range(0, 10, ErrorMessage = "Điểm phải từ 0 đến 10")]
        public decimal? DiemLan1 { get; set; }

        [Range(0, 10, ErrorMessage = "Điểm phải từ 0 đến 10")]
        public decimal? DiemLan2 { get; set; }
    }
}

