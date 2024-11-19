/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parsers;

/**
 *
 * @author armax
 */
public interface AbstractCustomRoot {

    public String[] getModes();

    public Pkg_box newRoot(Class root, int mode);

}
