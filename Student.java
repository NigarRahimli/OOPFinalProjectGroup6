
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

 
 public class Student {
        List<Integer> preferences;
        int studentId, destinationId;
        Random random = new Random();
        private static final int MAX_NUMBER_OF_DESTINATIONS_PER_STUDENT = 5;
        public Student(int studentId, List<Integer> preferences, int destinationId) {
            this.studentId = studentId;
            this.preferences = preferences;
            this.destinationId = destinationId;
        }

        public Student() {
            preferences = new ArrayList<>();
            for (int i = 0; i < MAX_NUMBER_OF_DESTINATIONS_PER_STUDENT; i++) {
                preferences.add(random.nextInt(10));
            }
        }
    }