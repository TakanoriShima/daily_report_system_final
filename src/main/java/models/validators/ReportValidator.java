package models.validators;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import actions.views.ReportView;
import constants.MessageConst;

/**
 * 日報インスタンスに設定されている値のバリデーションを行うクラス
 */
public class ReportValidator {

	/**
	 * 日報インスタンスの各項目についてバリデーションを行う
	 * @param rv 日報インスタンス
	 * @return エラーのリスト
	 */
	public static List<String> validate(ReportView rv) {
		List<String> errors = new ArrayList<String>();

		//タイトルのチェック
		String titleError = validateTitle(rv.getTitle());
		if (!titleError.equals("")) {
			errors.add(titleError);
		}

		//内容のチェック
		String contentError = validateContent(rv.getContent());
		if (!contentError.equals("")) {
			errors.add(contentError);
		}

		// 出勤時間の入力チェック
		String startTimeError = validateStartTime(rv.getStart_time());
		if (!startTimeError.equals("")) {
			errors.add(startTimeError);
		}

		// 退勤時間の入力チェック
		String endTimeError = validateEndTime(rv.getEnd_time());
		if (!endTimeError.equals("")) {
			errors.add(endTimeError);
		}

		// 出勤時間も退勤時間も入力されていた時
		if(startTimeError.equals("") && endTimeError.equals("")) {
			// 出勤時間と退勤時間の前後関係のチェック
			String startEndTimeOrderError = validateStartEndTimeOrder(rv.getStart_time(), rv.getEnd_time());
			if (!startEndTimeOrderError.equals("")) {
				errors.add(startEndTimeOrderError);
			}
		}

		return errors;
	}

	/**
	 * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージを返却
	 * @param title タイトル
	 * @return エラーメッセージ
	 */
	private static String validateTitle(String title) {
		if (title == null || title.equals("")) {
			return MessageConst.E_NOTITLE.getMessage();
		}

		//入力値がある場合は空文字を返却
		return "";
	}

	/**
	 * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
	 * @param content 内容
	 * @return エラーメッセージ
	 */
	private static String validateContent(String content) {
		if (content == null || content.equals("")) {
			return MessageConst.E_NOCONTENT.getMessage();
		}

		//入力値がある場合は空文字を返却
		return "";
	}

	/**
	 * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
	 * @param start_time 出勤時間
	 * @return エラーメッセージ
	 */
	private static String validateStartTime(String start_time) {
		Pattern p = Pattern.compile("^([0-1][0-9]|[2][0-3]):[0-5][0-9]$");
		Matcher m = p.matcher(start_time);
		if (!m.find()) {
			return "出勤時間を正しく入力してください";
		} else {
			return "";
		}
	}

	/**
	 * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
	 * @param start_time 退勤時間
	 * @return エラーメッセージ
	 */
	private static String validateEndTime(String end_time) {
		Pattern p = Pattern.compile("^([0-1][0-9]|[2][0-3]):[0-5][0-9]$");
		Matcher m = p.matcher(end_time);
		if (!m.find()) {
			return "退勤時間を正しく入力してください";
		} else {
			return "";
		}
	}

	/**
	 * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
	 * @param start_time 退勤時間, end_time 退勤時間
	 * @return エラーメッセージ
	 */
	private static String validateStartEndTimeOrder(String start_time, String end_time) {
		System.out.println("順序！！！");
		Time start = Time.valueOf(start_time + ":00");
		Time end = Time.valueOf(end_time + ":00");
		if (start.after(end)) {
			System.out.println("順序違う");
			return "出勤時間と退勤時間の入力が逆になっていないか確認してください";
		} else {
			System.out.println("順序正しい");
			return "";
		}
	}
}
