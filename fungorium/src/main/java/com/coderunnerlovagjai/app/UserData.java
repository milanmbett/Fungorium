package com.coderunnerlovagjai.app;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class UserData extends AbstractTableModel
{
    List<User> uR = new ArrayList<User>();

    @Override
    public int getRowCount()
    {
        return uR.size();
    }
    @Override
    public int getColumnCount()
    {
        return 3;
    }
    @Override
        public Object getValueAt(int rowIndex, int columnIndex)
    {
        User u = uR.get(rowIndex);
        switch (columnIndex) 
        {
            case 0:
                return u.getName();
            case 1:
                return u.getPoints();
            default:
                return u.getDestroyedTecton_Base();
        }
    }
    @Override
    public String getColumnName(int column)
    {
        switch (column) {
            case 0:
                return "Player Name";
            case 1:
                return "Points";
            default:
                return "Enemy base destroyed?";
        }
    }
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex) 
        {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            default:
                return Boolean.class;
        }
    }
    public void addRecord(String n, int p, boolean d)
    {
        uR.add(new User(n, p, d));
    }
}
