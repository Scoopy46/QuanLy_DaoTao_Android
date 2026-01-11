using Microsoft.EntityFrameworkCore;
using StudentManager.Data;
using StudentManager.Models;

namespace StudentManager.Services
{
    public class UserService : IUserService
    {
        private readonly SinhVienDbContext _db;
        public UserService(SinhVienDbContext db) { _db = db; }

        public IEnumerable<User> GetAll() => _db.User.AsNoTracking().ToList();

        public User GetById(int id) => _db.User.Find(id);

        public async Task<bool> CreateAsync(User user)
        {
            if (user == null) return false;
            if (await _db.User.AnyAsync(u => u.UserName == user.UserName)) return false;
            _db.User.Add(user);
            await _db.SaveChangesAsync();
            return true;
        }

        public async Task<bool> UpdateAsync(int id, User user)
        {
            var exist = await _db.User.FindAsync(id);
            if (exist == null) return false;
            exist.FullName = user.FullName;
            exist.Lop = user.Lop;
            exist.PassWord = user.PassWord;
            exist.Type = user.Type;
            await _db.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteAsync(int id)
        {
            var exist = await _db.User.FindAsync(id);
            if (exist == null) return false;
            _db.User.Remove(exist);
            await _db.SaveChangesAsync();
            return true;
        }
    }
}