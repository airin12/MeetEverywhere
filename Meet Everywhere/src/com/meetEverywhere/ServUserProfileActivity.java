package com.meetEverywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.InvitationMessage;
import com.meetEverywhere.common.Tag;
import com.meetEverywhere.common.User;

public class ServUserProfileActivity extends Activity {

	private EditText inviteMessage;
	private ImageView image;
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

		image = (ImageView) findViewById(R.id.send_icon2_profile);
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

	public void backToMainMenu(View view) {
		finish();
	}

	public void blockUser(View view) {
		user.setBlocked(true);
		refreshLayout();
		Toast.makeText(getApplicationContext(), "U¿ytkownik zablokowany",
				Toast.LENGTH_SHORT).show();
	}

	public void unlockUser(View view) {
		user.setBlocked(false);
		refreshLayout();
		Toast.makeText(getApplicationContext(), "U¿ytkownik odblokowany",
				Toast.LENGTH_SHORT).show();
	}

	private void refreshLayout() {
		if (user.isAcquaintance()) {
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
			((TextView) invitationReceivedLayout
					.findViewById(R.id.invitation_received_text)).setText(user
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

	public void deleteFriend(View view) {
		user.setAcquaintance(false);
		refreshLayout();

		Toast.makeText(getApplicationContext(), "Usuniêto z listy znajomych",
				Toast.LENGTH_SHORT).show();
	}

	public void sendInvite(View view) {
		/*
		 * user.changeInvited();
		 * 
		 * layout.setVisibility(LinearLayout.VISIBLE); visible=false;
		 * inviteMessage.setVisibility(EditText.GONE);
		 * image.setVisibility(EditText.GONE);
		 */
		InvitationMessage invite = new InvitationMessage(inviteMessage
				.getText().toString(), dispatcher.getOwnData().getNickname(),
				dispatcher.getOwnData().getUserID(), user.getUserID());
		// Configuration.getInstance().getInvitationMessagesSent().add(invite);

		user.setInvitationMessage(inviteMessage.getText().toString());

		user.sendInvitation(invite, inviteIconLayout, inviteMessage, image,
				user);
		user.setInvited(true);
		/*
		 * DAO dao = new DAO(); dao.sendInvite(user.getId(),
		 * inviteMessage.getText().toString());
		 */
	}

	public void openChat(View view) {
		Intent intent = new Intent(this, GeneralChat.class);
		intent.putExtra("nick", user.getNickname());
		startActivity(intent);
	}

	public void acceptInvite(View view) {
		user.setAcquaintance(true);
		user.setInvitationMessage(null);
		refreshLayout();
		Toast.makeText(getApplicationContext(), "Zaakceptowano zaproszenie",
				Toast.LENGTH_SHORT).show();

	}

	public void showMoreFromView(View view) {
		if (user.isInvited())
			Toast.makeText(getApplicationContext(),
					"Zaproszenie zosta³o ju¿ wys³ane", Toast.LENGTH_SHORT)
					.show();
		else {
			if (visible) {
				inviteMessage.setVisibility(EditText.GONE);
				image.setVisibility(EditText.GONE);
				visible = false;
			} else {
				inviteMessage.setVisibility(EditText.VISIBLE);
				image.setVisibility(EditText.VISIBLE);
				visible = true;
			}
		}
	}

}
