using StudentManager.Models;

namespace StudentManager.Services
{
    public interface ISinhVienService
    {
        IEnumerable<SinhVien> GetAll();
        SinhVien GetById(string maSv);
        Task<bool> CreateAsync(SinhVien sv);
        Task<bool> UpdateAsync(string maSv, SinhVien sv);
        Task<bool> DeleteAsync(string maSv);
    }
}