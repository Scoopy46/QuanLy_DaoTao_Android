using StudentManager.Models;
using StudentManager.ViewModels;

namespace StudentManager.Services
{
    public interface IDiemThiService
    {
        IEnumerable<DiemThiViewModel> GetByFilter(string? lop, string? maMH);
        Task<bool> CreateOrUpdateAsync(DiemThi diemThi);
        Task<bool> ValidateMaSVAsync(string maSV);
        Task<bool> ValidateMaMHAsync(string maMH);
    }
}

