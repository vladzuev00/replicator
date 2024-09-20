package by.aurorasoft.replicator.util;

import lombok.RequiredArgsConstructor;

import javax.lang.model.element.Name;

//TODO: use everywhere
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public final class NameImpl implements Name {
    private final String value;

    @Override
    public boolean contentEquals(CharSequence text) {
        return value.contentEquals(text);
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
}