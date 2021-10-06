package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * フォローについて画面の入力値・出力値を扱うViewモデル
 *
 */
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
public class FollowView {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * フォローする従業員
	 */
	private EmployeeView follow;

	/**
	 * フォローされる従業員
	 */
	private EmployeeView follower;

	/**
	 * 日報の内容
	 */
	private LocalDateTime createdAt;

	/**
	 * 出勤時間
	 */
	private LocalDateTime updatedAt;

}
