using Microsoft.EntityFrameworkCore;
using StudentManager.Data;
using StudentManager.Models;

namespace StudentManager.Services
{
    public class SinhVienService : ISinhVienService
    {
        private readonly SinhVienDbContext _db;
        public SinhVienService(SinhVienDbContext db) { _db = db; }

        public IEnumerable<SinhVien> GetAll() => _db.SinhVien.AsNoTracking().ToList();

        public SinhVien GetById(string maSv) => _db.SinhVien.Find(maSv);

        public async Task<bool> CreateAsync(SinhVien sv)
        {
            if (sv == null) return false;
            if (await _db.SinhVien.AnyAsync(x => x.MaSV == sv.MaSV)) return false;
            _db.SinhVien.Add(sv);
            await _db.SaveChangesAsync();
            return true;
        }

        public async Task<bool> UpdateAsync(string maSv, SinhVien sv)
        {
            var exist = await _db.SinhVien.FindAsync(maSv);
            if (exist == null) return false;
            exist.HoTen = sv.HoTen;
            exist.NamSinh = sv.NamSinh;
            exist.Lop = sv.Lop;
            exist.Anh = sv.Anh;
            await _db.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteAsync(string maSv)
        {
            var exist = await _db.SinhVien.FindAsync(maSv);
            if (exist == null) return false;
            _db.SinhVien.Remove(exist);
            await _db.SaveChangesAsync();
            return true;
        }
    }
}