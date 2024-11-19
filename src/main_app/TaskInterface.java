/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_app;

public interface TaskInterface {

    public void updateProgress(String values);

    public Object doInBackground(Object[] paramss);

    public boolean onPreExecute();

    public void onPostExecute(Object result);
}
