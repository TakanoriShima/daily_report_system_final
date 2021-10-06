package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeView;
import actions.views.FollowView;

/**
 * フォロー情報に設定されている値のバリデーションを行うクラス
 */
public class FollowValidator {

	/**
	 * フォローインスタンスの各項目についてバリデーションを行う
	 * @param fv 日報インスタンス
	 * @return エラーのリスト
	 */
	public static List<String> validate(FollowView fv) {
		List<String> errors = new ArrayList<String>();

		//タイトルのチェック
		String followError = validateFollow(fv.getFollow());
		if (!followError.equals("")) {
			errors.add(followError);
		}

		//内容のチェック
		String followerError = validateFollower(fv.getFollower());
		if (!followerError.equals("")) {
			errors.add(followerError);
		}

		return errors;
	}

	/**
	 * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージを返却
	 * @param title タイトル
	 * @return エラーメッセージ
	 */
	private static String validateFollow(EmployeeView follow_v) {
		//		if (title == null || title.equals("")) {
		//			return MessageConst.E_NOTITLE.getMessage();
		//		}

		//入力値がある場合は空文字を返却
		return "";
	}

	/**
	 * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
	 * @param content 内容
	 * @return エラーメッセージ
	 */
	private static String validateFollower(EmployeeView follower_v) {
		//		if (content == null || content.equals("")) {
		//			return MessageConst.E_NOCONTENT.getMessage();
		//		}

		//入力値がある場合は空文字を返却
		return "";
	}

}
