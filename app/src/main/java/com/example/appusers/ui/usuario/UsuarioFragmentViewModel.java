package com.example.appusers.ui.usuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appusers.modelos.usuarios;
import com.example.appusers.retrofit.httpCall;
import com.example.appusers.retrofit.onResponseUsuarios;

import java.util.List;

public class UsuarioFragmentViewModel extends ViewModel {
    private MutableLiveData<List<usuarios>> listUsers;
    private MutableLiveData<String> code;
    private MutableLiveData<String> mText;
    //private MutableLiveData<usuarios> selected;
    public UsuarioFragmentViewModel() {
        mText = new MutableLiveData<>();
        listUsers = new MutableLiveData<>();
        code = new MutableLiveData<>();
        //selected = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    //public void setSelected(usuarios item) {
    //    selected.setValue(item);
    //}
    public MutableLiveData<List<usuarios>> getUsers() {
       return listUsers;
    }
    //public MutableLiveData<usuarios> getSelected() {
     //   return selected;
    //}
    public LiveData<String> getText() {
        return mText;
    }
    public MutableLiveData<String> requestUsuarios() {
        httpCall.getUsuarios((list, Code) -> {
            switch (Code) {
                case "200":
                    listUsers.setValue(list);
                    code.setValue(Code);
                    break;
                case "null":
                    code.setValue(Code);
                    break;
                case "void":
                    code.setValue(Code);
                    break;
            }
        });
        return code;
    }
}