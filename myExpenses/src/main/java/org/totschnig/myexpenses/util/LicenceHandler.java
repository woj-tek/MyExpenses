package org.totschnig.myexpenses.util;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.VisibleForTesting;

import org.totschnig.myexpenses.BuildConfig;
import org.totschnig.myexpenses.MyApplication;
import org.totschnig.myexpenses.model.Account;
import org.totschnig.myexpenses.model.Template;
import org.totschnig.myexpenses.preference.PrefKey;
import org.totschnig.myexpenses.sync.GenericAccountService;

public abstract class LicenceHandler {
  public static boolean HAS_EXTENDED = !BuildConfig.FLAVOR.equals("blackberry");

  public void init() {
    if (PrefKey.CURRENT_VERSION.getInt(-1) != -1) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        AsyncTask.execute(() -> refresh(true));
      } else {
        refresh(true);
      }
    }
  }

  public abstract boolean isContribEnabled();

  public abstract boolean isExtendedEnabled();

  public void refresh(boolean invalidate) {
    refreshDo();
    if (invalidate) {
      invalidate();
    }
  }

  protected abstract void refreshDo();

  void invalidate() {
    Template.updateNewPlanEnabled();
    Account.updateNewAccountEnabled();
    GenericAccountService.updateAccountsIsSyncable();
  }

  @VisibleForTesting
  public void setLockState(boolean locked) {
    if (MyApplication.isInstrumentationTest()) {
      setLockStateDo(locked);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  protected abstract void setLockStateDo(boolean locked);
  
  public enum LicenceStatus {
    CONTRIB, EXTENDED
  }
}