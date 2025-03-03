#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

// Morse code dictionary
typedef struct {
	char letter;
	char *code;
    
} Morse;

// Sample Morse code dictionary (you can expand this as needed)
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

#define MORSE_DICT_SIZE (sizeof(morse_dict) / sizeof(Morse))

// Function to convert a string to uppercase
void to_uppercase(char *s) {
    for (; *s; ++s) *s = toupper((unsigned char)*s);
}

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
char* decode_morse(const char* morse) {
    // For simplicity, we'll assume that each Morse code letter is separated by a space
    // and words are separated by '/'
    char *copy = strdup(morse);
    if(!copy) return NULL;
    char *token = strtok(copy, " ");
    char *decoded = malloc(strlen(morse) + 1); // Approximation
    if(!decoded) {
        free(copy);
        return NULL;
    }
    decoded[0] = '\0';
    while(token != NULL) {
        for(int i = 0; i < MORSE_DICT_SIZE; i++) {
            if(strcmp(morse_dict[i].code, token) == 0) {
                strcat(decoded, (char[2]){morse_dict[i].letter, '\0'});
                break;
            }
        }
        token = strtok(NULL, " ");
    }
    free(copy);
    return decoded;
}

// Function to generate all variations with spaces
void generate_variants(char *s, int length, char *buffer, int pos, int spaces, int max_splits, char ***variants, int *count) {
    if(spaces == max_splits) {
        buffer[pos] = '\0';
        // Append the rest of the string
        strcat(buffer, &s[pos]);
        // Allocate memory for the new variant
        char *variant = strdup(buffer);
        if(variant == NULL) {
            fprintf(stderr, "Memory allocation failed\n");
            exit(1);
        }
        (*variants)[(*count)++] = variant;
        return;
    }
    if(pos >= length) return;
    // Option 1: Insert space after current character
    buffer[pos++] = s[pos-1];
    buffer[pos++] = ' ';
    generate_variants(s, length, buffer, pos, spaces + 1, max_splits, variants, count);
    pos -= 2;
    // Option 2: Do not insert space
    buffer[pos++] = s[pos-1];
    generate_variants(s, length, buffer, pos, spaces, max_splits, variants, count);
}

int main() {
    char input[] = "--.-------...-.-----..-.--...."; // Example input
    int length = strlen(input);
    int max_splits = length - 1; // Maximum number of spaces

    // Estimate the number of variants: 2^(length-1)
    unsigned long long total_variants = 1ULL << (length - 1);
    printf("Total variants to process: %llu\n", total_variants);

    // Allocate memory for variants
    char ***variants = malloc(sizeof(char**) * total_variants);
    if(variants == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return 1;
    }
    for(unsigned long long i = 0; i < total_variants; i++) {
        variants[i] = NULL;
    }

    // Generate all variants
    char buffer[61]; // 30 characters + 29 spaces + '\0'
    int count = 0;
    generate_variants(input, length, buffer, 1, 0, max_splits, &variants, &count);

    // Decode each variant
    for(unsigned long long i = 0; i < count; i++) {
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
