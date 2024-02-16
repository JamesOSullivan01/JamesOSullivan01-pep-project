package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDao;
    
    public AccountService(){
        accountDao = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
            this.accountDao = accountDAO;
    }

    public Account addAccount(Account account){
        Account addedAccount = accountDao.registerNewUser(account);
        if(addedAccount != null){
            return addedAccount;
        } else {
            return null;
        }
    }
}
