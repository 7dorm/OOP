#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

// Morse code dictionary structure
typedef struct {
    char letter;
    char *code;
} Morse;

// Sample Morse code dictionary
Morse morse_dict[] = {
    {'A', ".-"}, {'B', "-..."}, {'C', "-.-."}, {'D', "-.."}, {'E', "."},
    {'F', "..-."}, {'G', "--."}, {'H', "...."}, {'I', ".."}, {'J', ".---"},
    {'K', "-.-"}, {'L', ".-.."}, {'M', "--"}, {'N', "-."}, {'O', "---"},
    {'P', ".--."}, {'Q', "--.-"}, {'R', ".-."}, {'S', "..."}, {'T', "-"},
    {'U', "..-"}, {'V', "...-"}, {'W', ".--"}, {'X', "-..-"}, {'Y', "-.--"},
    {'Z', "--.."},
    {'0', "-----"}, {'1', ".----"}, {'2', "..---"}, {'3', "...--"}, {'4', "....-"},
    {'5', "....."}, {'6', "-...."}, {'7', "--..."}, {'8', "---.."}, {'9', "----."},
    {'&', ".-..."}, {'\'', ".----."}, {'@', ".--.-."}, {')', "-.--.-"}, {'(', "-.--."},
    {':', "---..."}, {',', "--..--"}, {'=', "-...-"}, {'!', "-.-.--"}, {'.', ".-.-.-"},
    {'-', "-....-"}, {'+', ".-.-."}, {'"', ".-..-."}, {'?', "..--.."}, {'/', "-..-."},
    {' ', "/"}  // Assuming space is represented by '/'
};

// Number of entries in the Morse code dictionary
#define MORSE_DICT_SIZE (sizeof(morse_dict) / sizeof(Morse))

// Function to find Morse code for a given letter
char* get_morse(char letter) {
    for(int i = 0; i < MORSE_DICT_SIZE; i++) {
        if(morse_dict[i].letter == letter) {
            return morse_dict[i].code;
        }
    }
    return NULL;
}

// Function to decode a Morse code string
// Assumes that each Morse code letter is separated by a space
// and words are separated by '/'
char* decode_morse(const char* morse) {
    // Allocate memory for the decoded string
    // Maximum possible length is twice the input length
    size_t len = strlen(morse);
    char *decoded = malloc(len + 1); // +1 for null terminator
    if(!decoded) {
        fprintf(stdout, "Memory allocation failed\n");
        exit(1);
    }
    decoded[0] = '\0';

    // Tokenize the Morse code string by space
    char *copy = strdup(morse);
    if(!copy) {
        fprintf(stdout, "Memory allocation failed\n");
        free(decoded);
        exit(1);
    }
    char *token = strtok(copy, " ");
    while(token != NULL) {
        // Find the corresponding letter in the dictionary
        int found = 0;
        for(int i = 0; i < MORSE_DICT_SIZE; i++) {
            if(strcmp(morse_dict[i].code, token) == 0) {
                strncat(decoded, (char[2]){morse_dict[i].letter, '\0'}, 1);
                found = 1;
                break;
            }
        }
        if(!found) {
            // If not found, append a '?' to indicate an unknown character
            strcat(decoded, "?");
        }
        token = strtok(NULL, " ");
    }
    free(copy);

    // Replace '/' with space
    for(int i = 0; decoded[i] != '\0'; i++) {
        if(decoded[i] == '/') {
            decoded[i] = ' ';
        }
    }

    return decoded;
}

// Function to generate all variations with spaces
void generate_variants(const char *morse, int length, char *buffer, int pos, int spaces, int max_splits, char ***variants, int *count) {
    if(spaces == max_splits) {
        buffer[pos] = '\0';
        // Append the rest of the string
        strcat(buffer, &morse[pos]);
        // Allocate memory for the new variant
        char *variant = strdup(buffer);
        if(variant == NULL) {
            fprintf(stdout, "Memory allocation failed\n");
            exit(1);
        }
        (*variants)[(*count)++] = variant;
        return;
    }
    if(pos >= length) return;
    // Option 1: Insert space after current character
    buffer[pos++] = morse[pos-1];
    buffer[pos++] = ' ';
    generate_variants(morse, length, buffer, pos, spaces + 1, max_splits, variants, count);
    pos -= 2;
    // Option 2: Do not insert space
    buffer[pos++] = morse[pos-1];
    generate_variants(morse, length, buffer, pos, spaces, max_splits, variants, count);
}

int main() {
    // Example Morse code input
    char morse_input[] = "--.-------...-.-----..-.--";
    int length = strlen(morse_input);
    int max_splits = length; // Maximum number of spaces to insert

    // Estimate the number of variants: 2^length
    unsigned long long total_variants = 1ULL << length;
    printf("Total variants to process: %llu\n", total_variants);

    // Allocate memory for variants
    char ***variants = malloc(sizeof(char**) * total_variants);
    if(variants == NULL) {
        fprintf(stdout, "Memory allocation failed\n");
        return 1;
    }
    for(unsigned long long i = 0; i < total_variants; i++) {
        variants[i] = NULL;
    }
	printf("hell\n");
    // Generate all variants
    char buffer[61]; // 30 Morse characters + 30 spaces + '\0'
    int count = 0;
    generate_variants(morse_input, length, buffer, 0, 0, max_splits, &variants, &count);

    // Decode each variant
    for(unsigned long long i = 0; i < count; i++) {
    	printf("%s\n", variants[i]);
        char *decoded = decode_morse(variants[i]);
        if(decoded != NULL) {
            printf("Variant %llu: %s => %s\n", i+1, variants[i], decoded);
            free(decoded);
        }
        free(variants[i]);
    }
    free(variants);
    return 0;
}
