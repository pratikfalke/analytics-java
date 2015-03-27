package com.segment.analytics.messages;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.segment.analytics.internal.gson.AutoGson;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import static com.segment.analytics.internal.Utils.isNullOrEmpty;

/**
 * The group API call is how you associate an individual user with a group—be it a company,
 * organization, account, project, team or whatever other crazy name you came up with for the same
 * concept! It also lets you record custom traits about the group, like industry or number of
 * employees. Calling group is a slightly more advanced feature, but it’s helpful if you have
 * accounts with multiple users.
 * <p/>
 * Use {@link #builder} to construct your own instances.
 *
 * @see <a href="https://segment.com/docs/spec/group/">Group</a>
 */
@AutoValue @AutoGson public abstract class GroupMessage implements Message {

  /**
   * Start building an {@link GroupMessage} instance.
   *
   * @param groupId A unique identifier for the group in your database.
   * @throws IllegalArgumentException if the event name is null or empty
   * @see <a href="https://segment.com/docs/spec/group/#group-id">Group ID</a>
   */
  public static Builder builder(String groupId) {
    return new Builder(groupId);
  }

  public abstract String groupId();

  @Nullable public abstract Map<String, Object> traits();

  public Builder toBuilder() {
    return new Builder(this);
  }

  /** Fluent API for creating {@link GroupMessage} instances. */
  public static class Builder extends MessageBuilder<GroupMessage, Builder> {
    private String groupId;
    private Map<String, Object> traits;

    private Builder(GroupMessage group) {
      super(group);
      groupId = group.groupId();
      traits = group.traits();
    }

    private Builder(String groupId) {
      if (isNullOrEmpty(groupId)) {
        throw new IllegalArgumentException("groupId cannot be null or empty.");
      }
      this.groupId = groupId;
    }

    /**
     * Set a map of information you know about a group, like number of employees or website.
     *
     * @see <a href="https://segment.com/docs/spec/group/#traits">Traits</a>
     */
    public Builder traits(Map<String, Object> traits) {
      if (traits == null) {
        throw new NullPointerException("Null traits");
      }
      this.traits = ImmutableMap.copyOf(traits);
      return this;
    }

    @Override protected GroupMessage realBuild() {
      return new AutoValue_GroupMessage(Type.GROUP, UUID.randomUUID(), new Date(), context,
          anonymousId, userId, integrations, groupId, traits);
    }

    @Override Builder self() {
      return this;
    }
  }
}