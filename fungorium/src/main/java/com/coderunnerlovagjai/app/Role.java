package com.coderunnerlovagjai.app;
public interface Role //TODO: Megírás
{
    void on_turn(); 
    String getRoleName();
    void resetRole(Player player); // Reset the role for the next turn
    
}
