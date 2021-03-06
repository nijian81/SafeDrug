package cn.com.phhc.drugSafeApp.myAccount;

/**
 * 我的账户
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.com.phhc.drugSafeApp.R;

public class MyAccountFragment extends Fragment {

    int flag;
    RelativeLayout password_modify_relative_layout,exit_relative_layout,existing_account_login_relative_layout,register_relative_layout,opinionFeedback_relative_layout;
    View blue_line_upper_exit,blue_line_upper_register,blue_line_upper_existingAccountLogin;
    OpinionFeedbackFragment opinionFeedbackFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flag = getArguments().getInt("flag",0);
        View rootView = inflater.inflate(R.layout.my_account_fragment, container, false);
        if (flag == 0) {

        } else {
            password_modify_relative_layout = (RelativeLayout) rootView.findViewById(R.id.password_modify_relative_layout);
            password_modify_relative_layout.setVisibility(View.VISIBLE);
            exit_relative_layout = (RelativeLayout) rootView.findViewById(R.id.exit);
            exit_relative_layout.setVisibility(View.VISIBLE);
            blue_line_upper_exit = (View)rootView.findViewById(R.id.blue_line_upper_exit);
            blue_line_upper_exit.setVisibility(View.VISIBLE);
            blue_line_upper_existingAccountLogin = (View)rootView.findViewById(R.id.blue_line_upper_existingAccountLogin);
            blue_line_upper_existingAccountLogin.setVisibility(View.GONE);
            blue_line_upper_register = (View)rootView.findViewById(R.id.blue_line_upper_register);
            blue_line_upper_register.setVisibility(View.GONE);
            existing_account_login_relative_layout = (RelativeLayout) rootView.findViewById(R.id.existingAccountLogin);
            existing_account_login_relative_layout.setVisibility(View.GONE);
            register_relative_layout = (RelativeLayout) rootView.findViewById(R.id.register);
            register_relative_layout.setVisibility(View.GONE);
        }
        opinionFeedback_relative_layout = (RelativeLayout)rootView.findViewById(R.id.opinionFeedback);
        opinionFeedback_relative_layout.setOnClickListener(new MyClickJumpIntoOpinionFeedbackListener());

        return rootView;

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    //跳转至意见反馈fragment 尼见 2015-02-28
    class MyClickJumpIntoOpinionFeedbackListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            opinionFeedbackFragment = new OpinionFeedbackFragment();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.replace(R.id.login_fragment, opinionFeedbackFragment, "opinionFeedbackFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
