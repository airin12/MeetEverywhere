package com.meetEverywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.*;
import com.meetEverywhere.common.messages.FinishAcquiantanceMessage;
import com.meetEverywhere.common.messages.InvitationAcceptedMessage;
import com.meetEverywhere.common.messages.InvitationMessage;

public class ServUserProfileActivity extends Activity implements NotifiableLayoutElement {

	private EditText inviteMessage;
	private ImageView sendInvitationImage;
	private LinearLayout inviteIconLayout;
	private User user;
	private boolean visible;
	private BluetoothDispatcher dispatcher = BluetoothDispatcher.getInstance();
	private LinearLayout friendLayout;
	private LinearLayout inviteLayout;
	private LinearLayout deleteLayout;
	private LinearLayout blockLayout;
	private LinearLayout unlockLayout;
	private LinearLayout blockIconLayout;
	private LinearLayout invitationReceivedLayout;
	private LinearLayout confirmInviteLayout;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.serv_user_profile_layout);
		user = dispatcher.getTempUserHolder();
		inviteIconLayout = (LinearLayout) findViewById(R.id.invited_layout);
		blockIconLayout = (LinearLayout) findViewById(R.id.block_icon_layout);

		sendInvitationImage = (ImageView) findViewById(R.id.send_icon2_profile);
		inviteMessage = (EditText) findViewById(R.id.invite_message);
		visible = false;

		TextView nickname = (TextView) findViewById(R.id.nickname_profile);
		nickname.setText(user.getNickname());

		friendLayout = (LinearLayout) findViewById(R.id.friend_layout);
		inviteLayout = (LinearLayout) findViewById(R.id.invite_friend);
		deleteLayout = (LinearLayout) findViewById(R.id.delete_friend_row);
		blockLayout = (LinearLayout) findViewById(R.id.block_user_row);
		unlockLayout = (LinearLayout) findViewById(R.id.unlock_user_row);
		invitationReceivedLayout = (LinearLayout) findViewById(R.id.invitation_icon_layout);
		confirmInviteLayout = (LinearLayout) findViewById(R.id.confirm_button_row);

		refreshLayout();

		TextView text = (TextView) findViewById(R.id.user_desc_profile);
		text.setText(user.getDescription());

		TextView text2 = (TextView) findViewById(R.id.tags_profile);
		StringBuffer buffer = new StringBuffer();

		boolean start = true;
		if (user.getHashTags() != null) {
			for (Tag tag : user.getHashTags()) {

				if (start)
					start = false;
				else
					buffer.append(", ");

				buffer.append(tag.getName());
			}
		}

		text2.setText(buffer.toString());

	}

	@Override
	public void onResume() {
		super.onResume();
		DataChangedNotificationService.register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataChangedNotificationService.unregister(this);
	}

	public void backToMainMenu(View view) {
		finish();
	}

	public void blockUser(View view) {
		DAO dao = new DAO();
		if (dao.blockUser(user.getUserID())) {
			user.setBlocked(true);
			Toast.makeText(getApplicationContext(), "U¿ytkownik zablokowany", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Wyst¹pi³ b³¹d.", Toast.LENGTH_SHORT).show();
		}
		refreshLayout();
	}

	public void unblockUser(View view) {
		DAO dao = new DAO();
		if (dao.unblockUser(user.getUserID())) {
			user.setBlocked(false);
			Toast.makeText(getApplicationContext(), "U¿ytkownik odblokowany", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Wyst¹pi³ b³¹d.", Toast.LENGTH_SHORT).show();
		}
		refreshLayout();
	}

	public void acceptInvite(View view) {
		DAO dao = new DAO();
		User myself = Configuration.getInstance().getUser();
		//if (dao.acceptInvitaion(user.getUserID())) { //TODO: odblokowac jak bedzie dzialac accept po userId
			user.sendMessage(
			        new InvitationAcceptedMessage("", myself.getNickname(), myself.getUserID(), user.getUserID()), this);
		//} else {
		//	Toast.makeText(getApplicationContext(), "Wyst¹pi³ b³¹d.", Toast.LENGTH_SHORT).show();
		//}
	}

	public void rejectInvite(View view) {
		DAO dao = new DAO();
		if (!dao.rejectInvitaion(user.getUserID())) {
			Toast.makeText(getApplicationContext(), "Wyst¹pi³ b³¹d.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void deleteFriend(View view) {
		DAO dao = new DAO();
		User myself = Configuration.getInstance().getUser();
		//if(dao.removeFriend(user.getUserID())) {
			user.sendMessage(new FinishAcquiantanceMessage("", myself.getNickname(), myself.getUserID(), user.getUserID()),
					this);			
		//} else {
		//	Toast.makeText(getApplicationContext(), "Wyst¹pi³ b³¹d.", Toast.LENGTH_SHORT).show();
		//}
	}

	public void refreshLayout() {
		if (user.isAcquaintance()) {
			inviteMessage.setVisibility(LinearLayout.GONE);
			sendInvitationImage.setVisibility(LinearLayout.GONE);
			friendLayout.setVisibility(LinearLayout.VISIBLE);
			inviteLayout.setVisibility(LinearLayout.GONE);
			deleteLayout.setVisibility(LinearLayout.VISIBLE);
			blockLayout.setVisibility(LinearLayout.GONE);
			unlockLayout.setVisibility(LinearLayout.GONE);
			inviteIconLayout.setVisibility(LinearLayout.GONE);
			blockIconLayout.setVisibility(LinearLayout.GONE);
			invitationReceivedLayout.setVisibility(LinearLayout.GONE);
			confirmInviteLayout.setVisibility(LinearLayout.GONE);
		} else if (user.isBlocked()) {
			friendLayout.setVisibility(LinearLayout.GONE);
			inviteLayout.setVisibility(LinearLayout.GONE);
			deleteLayout.setVisibility(LinearLayout.GONE);
			blockLayout.setVisibility(LinearLayout.GONE);
			unlockLayout.setVisibility(LinearLayout.VISIBLE);
			inviteIconLayout.setVisibility(LinearLayout.GONE);
			blockIconLayout.setVisibility(LinearLayout.VISIBLE);
			invitationReceivedLayout.setVisibility(LinearLayout.GONE);
			confirmInviteLayout.setVisibility(LinearLayout.GONE);
		} else if (user.isInvited()) {
			inviteMessage.setVisibility(LinearLayout.GONE);
			sendInvitationImage.setVisibility(LinearLayout.GONE);
			friendLayout.setVisibility(LinearLayout.GONE);
			inviteLayout.setVisibility(LinearLayout.VISIBLE);
			deleteLayout.setVisibility(LinearLayout.GONE);
			blockLayout.setVisibility(LinearLayout.GONE);
			unlockLayout.setVisibility(LinearLayout.GONE);
			inviteIconLayout.setVisibility(LinearLayout.VISIBLE);
			blockIconLayout.setVisibility(LinearLayout.GONE);
			invitationReceivedLayout.setVisibility(LinearLayout.GONE);
			confirmInviteLayout.setVisibility(LinearLayout.GONE);
		} else if (user.getInvitationMessage() != null) {
			friendLayout.setVisibility(LinearLayout.GONE);
			inviteLayout.setVisibility(LinearLayout.GONE);
			deleteLayout.setVisibility(LinearLayout.GONE);
			blockLayout.setVisibility(LinearLayout.VISIBLE);
			unlockLayout.setVisibility(LinearLayout.GONE);
			inviteIconLayout.setVisibility(LinearLayout.GONE);
			blockIconLayout.setVisibility(LinearLayout.GONE);
			invitationReceivedLayout.setVisibility(LinearLayout.VISIBLE);
			confirmInviteLayout.setVisibility(LinearLayout.VISIBLE);
			((TextView) invitationReceivedLayout.findViewById(R.id.invitation_received_text)).setText(user
			        .getInvitationMessage());
		} else {
			friendLayout.setVisibility(LinearLayout.GONE);
			inviteLayout.setVisibility(LinearLayout.VISIBLE);
			deleteLayout.setVisibility(LinearLayout.GONE);
			blockLayout.setVisibility(LinearLayout.VISIBLE);
			unlockLayout.setVisibility(LinearLayout.GONE);
			inviteIconLayout.setVisibility(LinearLayout.GONE);
			blockIconLayout.setVisibility(LinearLayout.GONE);
			invitationReceivedLayout.setVisibility(LinearLayout.GONE);
			confirmInviteLayout.setVisibility(LinearLayout.GONE);
		}
	}

	public void sendInvite(View view) {
		User myself = Configuration.getInstance().getUser();
		user.sendMessage(
		        new InvitationMessage(inviteMessage.getText().toString(), myself.getNickname(), myself.getUserID(),
		                user.getUserID()), this);
	}

	public void openChat(View view) {
		Intent intent = new Intent(this, GeneralChat.class);
		intent.putExtra("nick", user.getNickname());
		startActivity(intent);
	}

	public void showMoreFromView(View view) {
		if (user.isInvited())
			Toast.makeText(getApplicationContext(), "Zaproszenie zosta³‚o ju¿ wys³ane", Toast.LENGTH_SHORT).show();
		else {
			if (visible) {
				inviteMessage.setVisibility(EditText.GONE);
				sendInvitationImage.setVisibility(EditText.GONE);
				visible = false;
			} else {
				inviteMessage.setVisibility(EditText.VISIBLE);
				sendInvitationImage.setVisibility(EditText.VISIBLE);
				visible = true;
			}
		}
	}

	public void notifyDataChanged() {
		refreshLayout();
	}
}
