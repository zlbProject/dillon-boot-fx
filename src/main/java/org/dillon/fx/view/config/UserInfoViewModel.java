package org.dillon.fx.view.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.mapping.ModelWrapper;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dillon.fx.request.Request;
import org.dillon.fx.request.feign.client.SysUserFeign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInfoViewModel implements ViewModel {

    private ModelWrapper<SysUser> wrapper = new ModelWrapper<>();
    private StringProperty roles = new SimpleStringProperty();
    private StringProperty dept = new SimpleStringProperty();
    private Command command;

    public UserInfoViewModel() {
        command = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                getUserInfo();
            }
        }, true); //Async
        command.execute();
    }

    public void setPerson(SysUser sysUser) {
        wrapper.set(sysUser);
        wrapper.reload();
    }


    public StringProperty userNameProperty() {
        return wrapper.field("userName", SysUser::getUserName, SysUser::setUserName, "");
    }

    public StringProperty phonenumberProperty() {
        return wrapper.field("phonenumber", SysUser::getPhonenumber, SysUser::setPhonenumber, "");
    }

    public StringProperty emailProperty() {
        return wrapper.field("email", SysUser::getEmail, SysUser::setEmail, "");
    }


    public ObjectProperty createTimeProperty() {
        return wrapper.field("createTime", SysUser::getCreateTime, SysUser::setCreateTime, null);
    }
    public ObjectProperty<SysDept> deptProperty() {
        return wrapper.field("dept", SysUser::getDept, SysUser::setDept, null);
    }

    public String getRoles() {
        return roles.get();
    }

    public StringProperty rolesProperty() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles.set(roles);
    }

    private void getUserInfo() {
        Map<String, Object> ajaxResult = Request.connector(SysUserFeign.class).getInfo();
        Map map = (Map) ajaxResult.get("user");
        SysUser sysUser = BeanUtil.toBeanIgnoreCase(map, SysUser.class, true);
        Platform.runLater(() -> {
            setPerson(sysUser);
            roles.setValue(ajaxResult.get("roles")+"");
        });

    }

}