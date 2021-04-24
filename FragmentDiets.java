public class FragmentDiets extends Fragment {
    private EditText editTextDiets;
    private ActiveUserData activeUserData = ActiveUserData.getInstance();

    // This page is a workout page that allows the user to read and update their workouts.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diets, container, false);
        editTextDiets = (EditText) v.findViewById(R.id.editTextDiets);
        // Put's all the workouts to the editText for the user to read.
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(
                "DietList", Context.MODE_PRIVATE);
        String work = sharedPreferences.getString(activeUserData.getUsername(), "Diet");
        editTextDiets.setText(work);
        // Saves the changes made by the user automatically to the file.
        editTextDiets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String workouts = editTextDiets.getText().toString();
                SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences(
                        "DietList", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(activeUserData.getUsername(), workouts);
                editor.apply();
            }
        });
        return v;
    }
}
