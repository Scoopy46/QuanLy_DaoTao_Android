using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StudentManager.Models
{
    [Table("MonHoc")]
    public class MonHoc
    {
        [Key, StringLength(10)]
        public string MaMH { get; set; }

        [Required, StringLength(100)]
        public string TenMon { get; set; }
    }
}

