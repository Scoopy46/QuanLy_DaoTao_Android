using StudentManager.Models;

namespace StudentManager.Services
{
    public interface IUserService
    {
        IEnumerable<User> GetAll();
        User GetById(int id);
        Task<bool> CreateAsync(User user);
        Task<bool> UpdateAsync(int id, User user);
        Task<bool> DeleteAsync(int id);
    }
}