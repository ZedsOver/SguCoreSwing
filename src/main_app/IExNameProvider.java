/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main_app;

/**
 *
 * @author armax
 */
public class IExNameProvider {

    public static IExNameProvider def = new IExNameProvider();

    protected IExNameProvider()
    {

    }

    /**
     *
     * @param parent code parent
     * @param cus item
     * @param i index of current item
     * @return
     */
    public String getDetail(int parent, IndexHelper.IEntry cus, int i)
    {
        if (IndexHelper.isNamed(cus)) {
            return IndexHelper.getName(cus);
        }
        return null;
    }
}
