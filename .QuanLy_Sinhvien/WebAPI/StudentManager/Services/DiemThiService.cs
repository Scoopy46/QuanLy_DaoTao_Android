using Microsoft.EntityFrameworkCore;
using StudentManager.Data;
using StudentManager.Models;
using StudentManager.ViewModels;

namespace StudentManager.Services
{
    public class DiemThiService : IDiemThiService
    {
        private readonly SinhVienDbContext _db;

        public DiemThiService(SinhVienDbContext db)
        {
            _db = db ?? throw new ArgumentNullException(nameof(db));
        }

        public IEnumerable<DiemThiViewModel> GetByFilter(string? lop, string? maMH)
        {
            var query = from dt in _db.DiemThi
                        join sv in _db.SinhVien on dt.MaSV equals sv.MaSV
                        join mh in _db.MonHoc on dt.MaMH equals mh.MaMH
                        where (string.IsNullOrWhiteSpace(lop) || sv.Lop == lop)
                           && (string.IsNullOrWhiteSpace(maMH) || dt.MaMH == maMH)
                        select new DiemThiViewModel
                        {
                            MaSV = dt.MaSV,
                            MaMH = dt.MaMH,
                            DiemLan1 = dt.DiemLan1,
                            DiemLan2 = dt.DiemLan2,
                            HoTen = sv.HoTen,
                            TenMon = mh.TenMon
                        };

            return query.AsNoTracking().ToList();
        }

        public async Task<bool> CreateOrUpdateAsync(DiemThi diemThi)
        {
            if (diemThi == null) return false;
            if (string.IsNullOrWhiteSpace(diemThi.MaSV) || string.IsNullOrWhiteSpace(diemThi.MaMH)) 
                return false;

            // Validate foreign keys
            var sinhVienExists = await _db.SinhVien.AnyAsync(sv => sv.MaSV == diemThi.MaSV);
            var monHocExists = await _db.MonHoc.AnyAsync(mh => mh.MaMH == diemThi.MaMH);

            if (!sinhVienExists || !monHocExists) return false;

            var existing = await _db.DiemThi.FindAsync(diemThi.MaSV, diemThi.MaMH);
            
            if (existing == null)
            {
                _db.DiemThi.Add(diemThi);
            }
            else
            {
                existing.DiemLan1 = diemThi.DiemLan1;
                existing.DiemLan2 = diemThi.DiemLan2;
            }

            await _db.SaveChangesAsync();
            return true;
        }

        public async Task<bool> ValidateMaSVAsync(string maSV)
        {
            if (string.IsNullOrWhiteSpace(maSV)) return false;
            return await _db.SinhVien.AnyAsync(sv => sv.MaSV == maSV);
        }

        public async Task<bool> ValidateMaMHAsync(string maMH)
        {
            if (string.IsNullOrWhiteSpace(maMH)) return false;
            return await _db.MonHoc.AnyAsync(mh => mh.MaMH == maMH);
        }
    }
}

