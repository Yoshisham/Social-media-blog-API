package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    static int MIN_PASSWORD_LENGTH = 4;
    private AccountDAO accountDAO;

    /**
     * No-args constructor for creating new AccountService with a new AccountDAO.
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for AccountService when AccountDAO is provided.
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Process new user registration if these conditions are met:
     * 1) Username does not already exists
     * 2) Username is not blank
     * 3) Password is at least 4 characters long
     * @param account the account object to be added.
     * @return the registered account object.
     */
    public Account addAccount(Account account) {
        if (!accountDAO.findAccount(account.getUsername())){
            if (!account.getUsername().isBlank() && account.getPassword().length() >= MIN_PASSWORD_LENGTH) {
                return accountDAO.registerAccount(account); 
            }
        }
        return null;
    }

    /**
     * Process user logins
     * @param account the account to be checked.
     * @return the user account that successfully loged in.
     */
    public Account loginAccount(Account account) {
        return accountDAO.loginAccount(account);
    }
}
